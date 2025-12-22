package dev.slne.surf.clan.fallback.repository

import dev.slne.clan.api.invite.ClanInvite
import dev.slne.surf.clan.fallback.table.ClanInvitesTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

val clanInvitesRepository = ClanInvitesRepository()

class ClanInvitesRepository {
    suspend fun findInvitesByClanId(clanId: Long) = newSuspendedTransaction(Dispatchers.IO) {
        ClanInvitesTable.selectAll().where(ClanInvitesTable.clanId eq clanId).let { rows ->
            rows.map {
                ClanInvite(
                    invited = it[ClanInvitesTable.invited],
                    invitedByUuid = it[ClanInvitesTable.invitedBy],
                    createdAt = it[ClanInvitesTable.createdAt],
                    updatedAt = it[ClanInvitesTable.updatedAt]
                )
            }
        }
    }
}