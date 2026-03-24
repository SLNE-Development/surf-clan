package dev.slne.surf.clan.core.client.services

import com.google.auto.service.AutoService
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteAcceptResult
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.invite.ClanInviteService
import dev.slne.surf.clan.core.clan.CoreClanService
import dev.slne.surf.clan.core.client.rabbit.rabbitApi
import dev.slne.surf.clan.core.invite.ClanInviteImpl
import dev.slne.surf.clan.core.invite.CoreClanInviteService
import dev.slne.surf.clan.core.protocol.invite.accept.AcceptClanInviteRequestPacket
import dev.slne.surf.clan.core.protocol.invite.create.CreateClanInviteRequestPacket
import dev.slne.surf.clan.core.protocol.invite.delete.DeleteInviteClanInviteRequestPacket
import dev.slne.surf.clan.core.protocol.invite.findPendingByClanID.FindPendingInvitesByClanIDRequestPacket
import dev.slne.surf.clan.core.protocol.invite.findPendingByInvited.FindPendingInvitesByInvitedRequestPacket
import dev.slne.surf.clan.core.protocol.invite.findPendingByInvitedAndClanName.FindPendingInviteByInvitedPlayerAndClanNameRequestPacket
import java.util.*

@AutoService(ClanInviteService::class)
class ClientClanInviteServiceImpl : CoreClanInviteService {
    override suspend fun fetchPendingInvites(clanID: ULong): Set<ClanInviteImpl> {
        val request = FindPendingInvitesByClanIDRequestPacket(clanID)
        return rabbitApi.sendRequest(request).invites
    }

    override suspend fun getPendingInviteByPlayerAndClanName(
        invited: UUID,
        clanName: String
    ): ClanInvite? {
        val request = FindPendingInviteByInvitedPlayerAndClanNameRequestPacket(invited, clanName)
        return rabbitApi.sendRequest(request).invite
    }

    override suspend fun getPendingInvitesByPlayer(invited: UUID): List<ClanInvite> {
        val request = FindPendingInvitesByInvitedRequestPacket(invited)
        return rabbitApi.sendRequest(request).invites
    }

    override suspend fun createInvite(
        clanID: ULong,
        invitee: UUID,
        invitedBy: UUID
    ): ClanInviteResult {
        val request = CreateClanInviteRequestPacket(clanID, invitee, invitedBy)
        return rabbitApi.sendRequest(request).result
    }

    override suspend fun deleteInvite(clanID: ULong, invitee: UUID): Boolean {
        val request = DeleteInviteClanInviteRequestPacket(clanID, invitee)
        return rabbitApi.sendRequest(request).value
    }

    override suspend fun revokeInvite(invite: ClanInviteImpl): Boolean {
        return deleteInvite(invite.clanID, invite.invited)
    }

    override suspend fun acceptInvite(invite: ClanInviteImpl): ClanInviteAcceptResult {
        val request = AcceptClanInviteRequestPacket(invite.clanID, invite.invited, invite.invitedBy)
        val accepted = rabbitApi.sendRequest(request).value

        if (!accepted) {
            return ClanInviteAcceptResult.AlreadyInClan
        } else {
            ClientClanServiceImpl.get().invalidateCachedClanByID(invite.clanID)

            val clan = CoreClanService.findClanByID(invite.clanID) ?: error("Clan not found")
            return ClanInviteAcceptResult.Accepted(clan)
        }
    }

    companion object {
        fun get() = ClanInviteService.INSTANCE as ClientClanInviteServiceImpl
    }
}