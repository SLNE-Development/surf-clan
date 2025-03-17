package dev.slne.clan.velocity.extensions

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.VelocityClanPlugin

fun Player.findClan(): Clan? = ClanService.findClanByMemberUniqueId(uniqueId)

@Suppress("UNCHECKED_CAST")
fun <C : ClanInvite> Player.findClanInvites() =
    ClanService.findClanInvitesByMemberUniqueId(uniqueId).map { it as C }

fun Clan.hasPermission(player: Player, permission: ClanPermission): Boolean {
    val clanMember = members.find { it.player.uuid == player.uniqueId } ?: return false

    return clanMember.hasPermission(permission)
}

val ClanMember.proxyPlayerOrNull: Player?
    get() = VelocityClanPlugin.instance.server.getPlayer(player.uuid).orElse(null)

val ClanMember.proxyPlayer: Player
    get() = VelocityClanPlugin.instance.server.getPlayer(player.uuid)
        .orElseThrow { IllegalStateException("Player not found") }
