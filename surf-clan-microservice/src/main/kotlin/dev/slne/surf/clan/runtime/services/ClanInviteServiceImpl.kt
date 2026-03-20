package dev.slne.surf.clan.runtime.services

import com.google.auto.service.AutoService
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteAcceptResult
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.invite.ClanInviteService
import dev.slne.clan.core.clan.CoreClanService
import dev.slne.clan.core.invite.ClanInviteImpl
import dev.slne.clan.core.invite.CoreClanInviteService
import dev.slne.surf.clan.runtime.db.repository.ClanInviteRepository
import java.util.*

@AutoService(ClanInviteService::class)
class ClanInviteServiceImpl : CoreClanInviteService {
    override suspend fun fetchPendingInvites(clanID: ULong): Set<ClanInviteImpl> {
        return ClanInviteRepository.fetchPendingInvites(clanID)
    }

    override suspend fun getPendingInviteByPlayerAndClanName(
        invited: UUID,
        clanName: String
    ): ClanInvite? {
        return ClanInviteRepository.getPendingInviteByPlayerAndClanName(invited, clanName)
    }

    override suspend fun getPendingInvitesByPlayer(invited: UUID): List<ClanInvite> {
        return ClanInviteRepository.getPendingInvitesByPlayer(invited)
    }

    override suspend fun createInvite(
        clanID: ULong,
        invitee: UUID,
        invitedBy: UUID
    ): ClanInviteResult {
        return ClanInviteRepository.createInvite(clanID, invitee, invitedBy)
    }

    override suspend fun deleteInvite(clanID: ULong, invitee: UUID): Boolean {
        return ClanInviteRepository.deleteInvite(clanID, invitee)
    }

    override suspend fun acceptInvite(invite: ClanInviteImpl): ClanInviteAcceptResult {
        val accepted =
            ClanInviteRepository.acceptInvite(invite.id, invite.invited, invite.invitedBy)
        if (!accepted) {
            return ClanInviteAcceptResult.AlreadyInClan
        } else {
            ClanServiceImpl.get().invalidateCachedClanByID(invite.clanID)

            val clan = CoreClanService.findClanByID(invite.clanID) ?: error("Clan not found")
            return ClanInviteAcceptResult.Accepted(clan)
        }
    }

    override suspend fun revokeInvite(invite: ClanInviteImpl): Boolean {
        return deleteInvite(invite.clanID, invite.invited)
    }

    companion object {
        fun get() = ClanInviteService.INSTANCE as ClanInviteServiceImpl
    }
}