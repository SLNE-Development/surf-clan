package dev.slne.surf.clan.microservice.rpc

import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.clan.core.member.ClanMemberImpl
import dev.slne.surf.clan.core.rpc.ClanMemberRpcService
import dev.slne.surf.clan.microservice.db.repository.ClanMemberRepository
import java.util.*

object ClanMemberRpcServiceImpl : ClanMemberRpcService {
    override suspend fun createMember(
        clanID: ULong,
        player: UUID,
        role: ClanMemberRole,
        invitedBy: UUID?
    ): ClanMemberAddResult {
        return ClanMemberRepository.createMember(
            clanID = clanID,
            player = player,
            role = role,
            invitedBy = invitedBy
        )
    }

    override suspend fun deleteMember(clanID: ULong, player: UUID): Boolean {
        return ClanMemberRepository.deleteMember(clanID, player)
    }

    override suspend fun changeMemberRole(
        memberID: ULong,
        role: ClanMemberRole
    ): Boolean {
        return ClanMemberRepository.changeRole(memberID, role)
    }

    override suspend fun findMemberByUUID(uuid: UUID): ClanMemberImpl? {
        return ClanMemberRepository.findByUuid(uuid)
    }
}