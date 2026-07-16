package dev.slne.surf.clan.core.rpc

import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.surf.clan.core.invite.ClanInviteImpl
import dev.slne.surf.rabbitmq.api.rpc.RpcService
import java.util.*

@RpcService
interface ClanInviteRpcService {

    suspend fun acceptInvite(inviteID: ULong, invitee: UUID, invitedBy: UUID): Boolean
    suspend fun createInvite(clanID: ULong, invitee: UUID, invitedBy: UUID): ClanInviteResult
    suspend fun deleteInvite(clanID: ULong, invitee: UUID): Boolean

    suspend fun findPendingInvitesByClanId(clanID: ULong): Set<ClanInviteImpl>
    suspend fun findPendingInvitesByInvited(invited: UUID): List<ClanInviteImpl>
    suspend fun findPendingInviteByClanNameAndInvited(clanName: String, invited: UUID): ClanInviteImpl?
}