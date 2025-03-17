package dev.slne.clan.api.member

import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.player.ClanPlayer
import java.time.LocalDateTime

interface ClanMember {

    val player: ClanPlayer
    var role: ClanMemberRole

    val addedBy: ClanPlayer

    val createdAt: LocalDateTime?
    val updatedAt: LocalDateTime?

    fun hasPermission(clanPermission: ClanPermission): Boolean

}