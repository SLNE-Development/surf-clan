package dev.slne.surf.clan.microservice.db.repository

import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.surf.clan.core.invite.ClanInviteImpl
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

private val instance = requiredService<ClanInviteRepository>()

interface ClanInviteRepository {
    suspend fun fetchPendingInvites(clanID: ULong): Set<ClanInviteImpl>
    suspend fun getPendingInviteByPlayerAndClanName(
        invited: UUID,
        clanName: String
    ): ClanInviteImpl?

    suspend fun getPendingInvitesByPlayer(invited: UUID): List<ClanInviteImpl>
    suspend fun createInvite(clanID: ULong, invitee: UUID, invitedBy: UUID): ClanInviteResult
    suspend fun deleteInvite(clanID: ULong, invitee: UUID): Boolean

    suspend fun acceptInvite(inviteID: ULong, invitee: UUID, invitedBy: UUID): Boolean

    companion object : ClanInviteRepository by instance {
        val INSTANCE get() = instance
    }
}