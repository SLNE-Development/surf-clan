package dev.slne.clan.core.member

import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.member.ClanMemberService
import java.util.*

interface CoreClanMemberService : ClanMemberService {
    suspend fun addMember(
        clanID: ULong,
        player: UUID,
        role: ClanMemberRole,
        invitedBy: UUID?
    ): ClanMemberAddResult

    suspend fun removeMember(clanID: ULong, player: UUID): Boolean
    suspend fun changeRole(member: ClanMemberImpl, role: ClanMemberRole): Boolean

    companion object : CoreClanMemberService by ClanMemberService.INSTANCE as CoreClanMemberService
}