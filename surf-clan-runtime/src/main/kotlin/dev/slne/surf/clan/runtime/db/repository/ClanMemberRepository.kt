package dev.slne.surf.clan.runtime.db.repository

import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.member.ClanMemberImpl
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

interface ClanMemberRepository {

    suspend fun createMember(clanID: ULong, player: UUID, role: ClanMemberRole, invitedBy: UUID?): ClanMemberAddResult
    suspend fun deleteMember(clanID: ULong, player: UUID): Boolean

    suspend fun changeRole(memberID: ULong, role: ClanMemberRole): Boolean

    suspend fun findByUuid(uuid: UUID): ClanMemberImpl?

    companion object : ClanMemberRepository by INSTANCE
}

private val INSTANCE = requiredService<ClanMemberRepository>()