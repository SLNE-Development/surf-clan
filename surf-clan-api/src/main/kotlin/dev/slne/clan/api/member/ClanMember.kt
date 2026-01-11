package dev.slne.clan.api.member

import dev.slne.clan.api.member.listener.ClanMemberListener
import dev.slne.clan.api.permission.ClanPermission
import java.util.*

interface ClanMember {
    val uuid: UUID
    val role: ClanMemberRole
    val addedBy: UUID?

    fun hasPermission(clanPermission: ClanPermission): Boolean
    suspend fun changeRole(role: ClanMemberRole): Boolean

    companion object {
        fun registerListener(listener: ClanMemberListener) = ClanMemberService.instance.registerListener(listener)
        fun unregisterListener(listener: ClanMemberListener) = ClanMemberService.instance.unregisterListener(listener)

        suspend fun byUuid(uuid: UUID) = ClanMemberService.instance.findMemberByUuid(uuid)
        suspend fun byName(name: String) = ClanMemberService.instance.findMemberByName(name)
    }
}