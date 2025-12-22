package dev.slne.clan.api.member

import dev.slne.clan.api.permission.ClanPermission
import java.time.LocalDateTime
import java.util.*

data class ClanMember(
    val uuid: UUID,
    var role: ClanMemberRole,

    val addedBy: UUID?,

    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = LocalDateTime.now()
) {
    fun hasPermission(clanPermission: ClanPermission) = role.hasPermission(clanPermission)
}