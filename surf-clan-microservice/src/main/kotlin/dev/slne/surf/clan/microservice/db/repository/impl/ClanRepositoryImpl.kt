package dev.slne.surf.clan.microservice.db.repository.impl

import com.google.auto.service.AutoService
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.api.clan.ClanTagColor
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.api.core.util.logger
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.member.ClanMemberImpl
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.clan.microservice.db.table.ClanMembersTable
import dev.slne.surf.clan.microservice.db.table.ClansTable
import dev.slne.surf.clan.microservice.db.table.SurfPlayersTable
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.*
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.*
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import kotlinx.coroutines.flow.toList
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import java.time.OffsetDateTime
import java.util.*

@AutoService(ClanRepository::class)
class ClanRepositoryImpl : ClanRepository {
    private val log = logger()

    private fun joinClansWithMembers() = ClansTable
        .leftJoin(ClanMembersTable, { ClansTable.id }, { ClanMembersTable.clanId })
        .leftJoin(SurfPlayersTable, { ClanMembersTable.uuid }, { SurfPlayersTable.uuid })

    override suspend fun findClanByPlayer(playerUuid: UUID): ClanImpl? = suspendTransaction {
        val clanIDSubQuery = ClanMembersTable
            .select(ClanMembersTable.clanId)
            .where { ClanMembersTable.uuid eq playerUuid }

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

    override suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl> =
        suspendTransaction {
            val cutoff = OffsetDateTime.now().minusSeconds(Clan.INACTIVE_AFTER.inWholeSeconds)
            val lastActiveAt = Coalesce(SurfPlayersTable.lastSeen, ClanMembersTable.createdAt)
            val activeMemberCount = Sum(
                Case()
                    .When(lastActiveAt greaterEq cutoff, intLiteral(1))
                    .Else(intLiteral(0)),
                IntegerColumnType()
            )

            joinClansWithMembers()
                .select(ClansTable.columns + activeMemberCount)
                .groupBy(ClansTable.id)
                .orderBy(activeMemberCount, SortOrder.DESC)
                .map { row ->
                    createClanDAO(row, emptySet())
                }
                .toList()
        }

    override suspend fun updateDescription(clanID: ULong, description: String?): Boolean =
        suspendTransaction {
            ClansTable.update({ ClansTable.id eq clanID }) {
                it[ClansTable.description] = description
            } > 0
        }

    override suspend fun updateDiscordInvite(clanID: ULong, discordInvite: String?): Boolean =
        suspendTransaction {
            ClansTable.update({ ClansTable.id eq clanID }) {
                it[ClansTable.discordInvite] = discordInvite
            } > 0
        }

    override suspend fun updateTagColor(
        clanID: ULong,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?
    ): Boolean = suspendTransaction {
        ClansTable.update({ ClansTable.id eq clanID }) {
            it[ClansTable.tagForegroundColor] = tagForegroundColor
            it[ClansTable.tagBackgroundColor] = tagBackgroundColor
            it[ClansTable.tagShadowColor] = tagShadowColor
        } > 0
    }

    override suspend fun create(
        name: String,
        tag: String,
        owner: UUID,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?,
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

        // Name and tag are checked up front because the caller needs to know which of the two
        // collided, and ON CONFLICT DO NOTHING cannot tell them apart.
        val nameTaken = ClansTable
            .select(ClansTable.id)
            .where { ClansTable.name eq name }
            .limit(1)
            .singleOrNull() != null

        if (nameTaken) {
            return@suspendTransaction ClanCreationResult.ClanNameAlreadyExists
        }

        val tagTaken = ClansTable
            .select(ClansTable.id)
            .where { ClansTable.tag eq tag }
            .limit(1)
            .singleOrNull() != null

        if (tagTaken) {
            return@suspendTransaction ClanCreationResult.ClanTagAlreadyExists
        }

        // The checks above leave a window for two concurrent creations, so still insert with
        // ON CONFLICT DO NOTHING. Catching the violation instead is not an option: PostgreSQL aborts
        // the whole transaction on a failed statement, and the following COMMIT would then fail with
        // PostgresqlRollbackException. Losing the name/tag distinction in that rare race is the
        // acceptable trade.
        val clanRow = ClansTable.insertReturning(ignoreErrors = true) { smt ->
            smt[this.name] = name
            smt[this.tag] = tag
            smt[this.createdBy] = owner
            tagForegroundColor?.let { smt[this.tagForegroundColor] = it }
            tagBackgroundColor?.let { smt[this.tagBackgroundColor] = it }
            tagShadowColor?.let { smt[this.tagShadowColor] = it }
            description?.let { smt[this.description] = it }
            discordInvite?.let { smt[this.discordInvite] = it }
        }.singleOrNull() ?: run {
            log.atWarning()
                .log("Clan %s (%s) lost a concurrent creation race for its name or tag", name, tag)
            return@suspendTransaction ClanCreationResult.ClanAlreadyExists
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
        clanID = row[ClansTable.id].value,
        uuid = row[ClansTable.uuid],
        name = row[ClansTable.name],
        tag = row[ClansTable.tag],
        createdByUuid = row[ClansTable.createdBy],
        description = row[ClansTable.description],
        discordInvite = row[ClansTable.discordInvite],
        clanTagColor = ClanTagColor.clanTagColorOrNull(
            foreground = row[ClansTable.tagForegroundColor],
            background = row[ClansTable.tagBackgroundColor],
            shadow = row[ClansTable.tagShadowColor]
        ),
        members = members,
        updatedAt = row[ClansTable.updatedAt],
        createdAt = row[ClansTable.createdAt]
    )
}