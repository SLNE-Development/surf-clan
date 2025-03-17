package dev.slne.clan.core.member

import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.player.ClanPlayer
import java.time.LocalDateTime

class CoreClanMember(
    override val player: ClanPlayer,
    override var role: ClanMemberRole,
    override val addedBy: ClanPlayer,

    override val createdAt: LocalDateTime? = LocalDateTime.now(),
    override val updatedAt: LocalDateTime? = LocalDateTime.now(),
) : ClanMember {
    override fun hasPermission(clanPermission: ClanPermission) = role.hasPermission(clanPermission)

    override fun toString(): String {
        return "CoreClanMember(player=$player, role=$role, addedBy=$addedBy, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}