package dev.slne.clan.api.member

import dev.slne.clan.api.member.listener.ClanMemberListener
import org.jetbrains.annotations.ApiStatus
import java.util.*

@ApiStatus.NonExtendable
interface ClanMember : ClanMemberView {
    suspend fun changeRole(role: ClanMemberRole): Boolean

    fun view(): ClanMemberView

    companion object {
        fun registerListener(listener: ClanMemberListener) = ClanMemberService.instance.registerListener(listener)
        fun unregisterListener(listener: ClanMemberListener) = ClanMemberService.instance.unregisterListener(listener)

        suspend fun byUuid(uuid: UUID) = ClanMemberService.instance.findMemberByUuid(uuid)
        suspend fun byName(name: String) = ClanMemberService.instance.findMemberByName(name)
    }
}