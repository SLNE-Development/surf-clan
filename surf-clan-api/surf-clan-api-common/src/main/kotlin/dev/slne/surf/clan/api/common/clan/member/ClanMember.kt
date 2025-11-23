package dev.slne.surf.clan.api.common.clan.member

import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import net.kyori.adventure.text.Component
import java.time.ZonedDateTime
import java.util.*

interface ClanMember : Comparable<ClanMember> {

    val uuid: UUID
    suspend fun clanPlayer(): ClanPlayer = ClanPlayer[uuid]

    val role: ClanMemberRole
    suspend fun setRole(role: ClanMemberRole, player: ClanPlayer): ComponentResult

    val addedByUuid: UUID
    suspend fun addedBy(): ClanPlayer = ClanPlayer[addedByUuid]

    val createdAt: ZonedDateTime
    val updatedAt: ZonedDateTime

    fun hasPermission(permission: ClanPermission): Boolean = role.hasPermission(permission)

    suspend fun asComponent(): Component
}