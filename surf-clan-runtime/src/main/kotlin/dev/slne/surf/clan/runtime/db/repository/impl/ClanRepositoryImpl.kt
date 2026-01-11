package dev.slne.surf.clan.runtime.db.repository.impl

import com.google.auto.service.AutoService
import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.member.ClanMemberImpl
import dev.slne.surf.clan.runtime.db.repository.ClanRepository
import dev.slne.surf.clan.runtime.db.table.ClanMembersTable
import dev.slne.surf.clan.runtime.db.table.ClansTable
import dev.slne.surf.database.libs.io.r2dbc.spi.R2dbcDataIntegrityViolationException
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.*
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.*
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import net.kyori.adventure.text.format.TextColor
import java.time.LocalDateTime
import java.util.*

@AutoService(ClanRepository::class)
class ClanRepositoryImpl : ClanRepository {
    private fun joinClansWithMembers() = ClansTable
        .leftJoin(ClanMembersTable, { ClansTable.id }, { ClanMembersTable.clanId })

    override suspend fun findClanByPlayer(playerUuid: UUID): ClanImpl? = suspendTransaction {
        val clanIDSubQuery = ClanMembersTable
            .select(ClanMembersTable.clanId)
            .where { ClanMembersTable.uuid eq playerUuid }
            .limit(1)

        joinClansWithMembers()
            .selectAll()
            .where { ClansTable.id inSubQuery clanIDSubQuery }
            .toList()
            .let(::createClanDAOOrNull)
    }

    override suspend fun findClanByUuid(clanUuid: UUID): ClanImpl? = suspendTransaction {
        joinClansWithMembers()
            .selectAll()
            .where { ClansTable.uuid eq clanUuid }
            .toList()
            .let(::createClanDAOOrNull)
    }

    override suspend fun findClanByTag(tag: String): ClanImpl? = suspendTransaction {
        joinClansWithMembers()
            .selectAll()
            .where { ClansTable.tag eq tag }
            .toList()
            .let(::createClanDAOOrNull)
    }

    override suspend fun findClanByID(id: ULong): ClanImpl? = suspendTransaction {
        joinClansWithMembers()
            .selectAll()
            .where { ClansTable.id eq id }
            .toList()
            .let(::createClanDAOOrNull)
    }

    override suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl> = suspendTransaction {
        val memberCount = ClanMembersTable.id.count()

        ClansTable
            .leftJoin(ClanMembersTable, { ClansTable.id }, { ClanMembersTable.clanId })
            .select(ClansTable.columns + memberCount)
            .groupBy(ClansTable.id)
            .orderBy(memberCount, SortOrder.DESC)
            .map { row ->
                createClanDAO(row, emptySet())
            }
            .toList()
    }

    override suspend fun updateDescription(clanID: ULong, description: String?): Boolean = suspendTransaction {
        ClansTable.update({ ClansTable.id eq clanID }) {
            it[ClansTable.description] = description
        } > 0
    }

    override suspend fun updateDiscordInvite(clanID: ULong, discordInvite: String?): Boolean = suspendTransaction {
        ClansTable.update({ ClansTable.id eq clanID }) {
            it[ClansTable.discordInvite] = discordInvite
        } > 0
    }

    override suspend fun updateTagColor(clanID: ULong, tagColor: TextColor): Boolean = suspendTransaction {
        ClansTable.update({ ClansTable.id eq clanID }) {
            it[ClansTable.tagColor] = tagColor
        } > 0
    }

    override suspend fun create(
        name: String,
        tag: String,
        owner: UUID,
        tagColor: TextColor?,
        description: String?,
        discordInvite: String?
    ): ClanCreationResult = suspendTransaction {
        val ownerAlreadyMember = ClanMembersTable
            .select(ClanMembersTable.id)
            .where { ClanMembersTable.uuid eq owner }
            .limit(1)
            .singleOrNull() != null

        if (ownerAlreadyMember) {
            return@suspendTransaction ClanCreationResult.OwnerIsAlreadyInClan
        }

        val clanRow = try {
            ClansTable.insertReturning { smt ->
                smt[this.name] = name
                smt[this.tag] = tag
                smt[this.createdBy] = owner
                tagColor?.let { smt[this.tagColor] = it }
                description?.let { smt[this.description] = it }
                discordInvite?.let { smt[this.discordInvite] = it }
            }.single()
        } catch (e: R2dbcDataIntegrityViolationException) {
            val msg = e.message ?: throw e
            return@suspendTransaction when {
                ClansTable.TAG_UQ_INDEX_NAME in msg -> ClanCreationResult.ClanTagAlreadyExists
                ClansTable.NAME_UQ_INDEX_NAME in msg -> ClanCreationResult.ClanNameAlreadyExists
                else -> throw e
            }
        }

        val ownerRow = ClanMembersTable.insertReturning {
            it[this.uuid] = owner
            it[this.addedBy] = owner
            it[this.role] = ClanMemberRole.OWNER
            it[this.clanId] = clanRow[ClansTable.id]
        }.single()

        val ownerMember = ClanMemberRepositoryImpl.createMemberDAO(ownerRow)
        val clan = createClanDAO(clanRow, setOf(ownerMember))

        ClanCreationResult.Success(clan)

    }

    override suspend fun delete(clanID: ULong): Boolean = suspendTransaction {
        ClansTable.deleteWhere { ClansTable.id eq clanID } > 0
    }

    override suspend fun suggestTagsByPrefix(prefix: String, limit: Int): List<String> {
        if (prefix.isBlank()) return emptyList()

        return suspendTransaction {
            ClansTable
                .select(ClansTable.tag)
                .where { ClansTable.tag like "$prefix%" }
                .orderBy(ClansTable.tag)
                .limit(limit)
                .map { it[ClansTable.tag] }
                .toList()
        }
    }

    fun createClanDAOOrNull(rows: List<ResultRow>): ClanImpl? {
        if (rows.isEmpty()) return null

        val clanRow = rows.first()
        val members = rows
            .mapNotNull { row ->
                if (row.getOrNull(ClanMembersTable.id) == null) return@mapNotNull null
                ClanMemberRepositoryImpl.createMemberDAO(row)
            }
            .toSet()

        return createClanDAO(clanRow, members)
    }

    fun createClanDAO(row: ResultRow, members: Set<ClanMemberImpl>): ClanImpl = ClanImpl(
        id = row[ClansTable.id].value,
        uuid = row[ClansTable.uuid],
        name = row[ClansTable.name],
        tag = row[ClansTable.tag],
        createdByUuid = row[ClansTable.createdBy],
        description = row[ClansTable.description],
        discordInvite = row[ClansTable.discordInvite],
        clanTagColor = row[ClansTable.tagColor],
        members = members,
        updatedAt = LocalDateTime.from(row[ClansTable.updatedAt]),
        createdAt = LocalDateTime.from(row[ClansTable.createdAt])
    )
}