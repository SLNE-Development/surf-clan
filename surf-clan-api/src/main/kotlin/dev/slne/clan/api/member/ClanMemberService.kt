package dev.slne.clan.api.member

import dev.slne.clan.api.member.listener.ClanMemberListener
import dev.slne.clan.api.util.InternalClanApi
import dev.slne.surf.api.core.util.requiredService
import java.util.*

private val service = requiredService<ClanMemberService>()

@InternalClanApi
interface ClanMemberService {
    fun registerListener(listener: ClanMemberListener)
    fun unregisterListener(listener: ClanMemberListener)

    suspend fun findMemberByUuid(uuid: UUID): ClanMember?
    suspend fun findMemberByName(name: String): ClanMember?

    companion object : ClanMemberService by service {
        val INSTANCE get() = service
    }
}