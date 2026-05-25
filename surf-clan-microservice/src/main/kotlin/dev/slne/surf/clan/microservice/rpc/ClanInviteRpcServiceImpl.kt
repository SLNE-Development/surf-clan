package dev.slne.surf.clan.microservice.rpc

import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.surf.clan.core.invite.ClanInviteImpl
import dev.slne.surf.clan.core.rpc.ClanInviteRpcService
import dev.slne.surf.clan.microservice.db.repository.ClanInviteRepository
import java.util.*

object ClanInviteRpcServiceImpl : ClanInviteRpcService {
    override suspend fun acceptInvite(
        inviteID: ULong,
        invitee: UUID,
        invitedBy: UUID
    ): Boolean {
        return ClanInviteRepository.acceptInvite(
            inviteID = inviteID,
            invitee = invitee,
            invitedBy = invitedBy
        )
    }

    override suspend fun createInvite(
        clanID: ULong,
        invitee: UUID,
        invitedBy: UUID
    ): ClanInviteResult {
        return ClanInviteRepository.createInvite(
            clanID = clanID,
            invitee = invitee,
            invitedBy = invitedBy
        )
    }

    override suspend fun deleteInvite(clanID: ULong, invitee: UUID): Boolean {
        return ClanInviteRepository.deleteInvite(clanID, invitee)
    }

    override suspend fun findPendingInvitesByClanId(clanID: ULong): Set<ClanInviteImpl> {
        return ClanInviteRepository.fetchPendingInvites(clanID)
    }

    override suspend fun findPendingInvitesByInvited(invited: UUID): List<ClanInviteImpl> {
        return ClanInviteRepository.getPendingInvitesByPlayer(invited)
    }

    override suspend fun findPendingInviteByClanNameAndInvited(
        clanName: String,
        invited: UUID
    ): ClanInviteImpl? {
        return ClanInviteRepository.getPendingInviteByPlayerAndClanName(
            invited = invited,
            clanName = clanName
        )
    }
}