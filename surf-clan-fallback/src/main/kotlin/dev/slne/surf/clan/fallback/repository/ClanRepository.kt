package dev.slne.surf.clan.fallback.repository

import dev.slne.clan.api.Clan
import dev.slne.surf.clan.fallback.table.ClanInvitesTable
import dev.slne.surf.clan.fallback.table.ClanMembersTable
import dev.slne.surf.clan.fallback.table.ClansTable
import dev.slne.surf.surfapi.core.api.util.toMutableObjectSet
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.*
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

val clanRepository = ClanRepository()

class ClanRepository {
    suspend fun findFirstByUuid(uuid: UUID): Clan? = newSuspendedTransaction(Dispatchers.IO) {
        ClansTable.selectAll().where(ClansTable.uuid eq uuid).firstOrNull()?.let { row ->
            createClanFromRow(row)
        }
    }

    suspend fun findFirstByTagIgnoreCase(tag: String): Clan? =
        newSuspendedTransaction(Dispatchers.IO) {
            ClansTable.selectAll().where(ClansTable.tag.lowerCase() eq tag.lowercase())
                .firstOrNull()
                ?.let { row ->
                    createClanFromRow(row)
                }
        }

    suspend fun findFirstByNameIgnoreCase(name: String): Clan? =
        newSuspendedTransaction(Dispatchers.IO) {
            ClansTable.selectAll().where(ClansTable.name.lowerCase() eq name.lowercase())
                .firstOrNull()
                ?.let { row ->
                    createClanFromRow(row)
                }
        }

    suspend fun findClans() = newSuspendedTransaction(Dispatchers.IO) {
        ClansTable.selectAll().map { row ->
            createClanFromRow(row)
        }.toObjectSet()
    }

    suspend fun findByMemberUuid(uuid: UUID): Clan? = newSuspendedTransaction(Dispatchers.IO) {
        findClans().firstOrNull { clan ->
            clan.members.any { member ->
                member.uuid == uuid
            }
        }
    }

    suspend fun existsByUuid(uuid: UUID): Boolean = newSuspendedTransaction(Dispatchers.IO) {
        ClansTable.selectAll().where(ClansTable.uuid eq uuid).count() > 0
    }


    suspend fun createClanFromRow(row: ResultRow): Clan {
        val invites =
            clanInvitesRepository.findInvitesByClanId(row[ClansTable.id].value)
                .toMutableObjectSet()
        val members =
            clanMembersRepository.findMembersByClanId(row[ClansTable.id].value)
                .toMutableObjectSet()

        return Clan(
            uuid = row[ClansTable.uuid],
            name = row[ClansTable.name],
            tag = row[ClansTable.tag],
            createdBy = row[ClansTable.createdBy],
            description = row[ClansTable.description],
            discordInvite = row[ClansTable.discordInvite],
            clanTagColor = row[ClansTable.color],
            invites = invites,
            members = members,

            createdAt = row[ClansTable.createdAt],
            updatedAt = row[ClansTable.updatedAt]
        )
    }

    suspend fun save(clan: Clan): Clan {
        newSuspendedTransaction(Dispatchers.IO) {
            ClansTable.upsert {
                it[uuid] = clan.uuid
                it[name] = clan.name
                it[tag] = clan.tag
                it[color] = clan.clanTagColor
                it[description] = clan.description
                it[createdBy] = clan.createdBy
                it[discordInvite] = clan.discordInvite
                it[createdAt] = clan.createdAt
                it[updatedAt] = clan.updatedAt
            }

            if (clan.invites.isNotEmpty()) {
                ClanInvitesTable.upsert {
                    for (invite in clan.invites) {
                        it[clanId] = ClansTable.selectAll().where(ClansTable.uuid eq clan.uuid)
                            .first()[ClansTable.id].value
                        it[invited] = invite.invited
                        it[invitedBy] = invite.invitedByUuid
                        it[createdAt] = invite.createdAt
                        it[updatedAt] = invite.updatedAt
                    }
                }
            }

            if (clan.members.isNotEmpty()) {
                ClanMembersTable.upsert {
                    for (member in clan.members) {
                        it[clanId] = ClansTable.selectAll().where(ClansTable.uuid eq clan.uuid)
                            .first()[ClansTable.id].value
                        it[uuid] = member.uuid
                        it[role] = member.role
                        it[addedBy] = member.addedBy
                        it[createdAt] = member.createdAt
                        it[updatedAt] = member.updatedAt
                    }
                }
            }
        }
        return clan
    }

    suspend fun delete(clan: Clan): Clan {
        newSuspendedTransaction(Dispatchers.IO) {
            val clanId = ClansTable.selectAll().where(ClansTable.uuid eq clan.uuid)
                .first()[ClansTable.id].value

            ClansTable.deleteWhere { ClansTable.uuid eq clan.uuid }
            ClanInvitesTable.deleteWhere { ClanInvitesTable.clanId eq clanId }
            ClanMembersTable.deleteWhere { ClanMembersTable.clanId eq clanId }
        }
        return clan
    }
}