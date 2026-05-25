package dev.slne.surf.clan.core.rpc

import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.clan.core.member.ClanMemberImpl
import dev.slne.surf.rabbitmq.api.rpc.RpcService
import java.util.*

@RpcService
interface ClanMemberRpcService {

    suspend fun createMember(clanID: ULong, player: UUID, role: ClanMemberRole, invitedBy: UUID?): ClanMemberAddResult
    suspend fun deleteMember(clanID: ULong, player: UUID): Boolean

    suspend fun changeMemberRole(memberID: ULong, role: ClanMemberRole): Boolean

    suspend fun findMemberByUUID(uuid: UUID): ClanMemberImpl?
}