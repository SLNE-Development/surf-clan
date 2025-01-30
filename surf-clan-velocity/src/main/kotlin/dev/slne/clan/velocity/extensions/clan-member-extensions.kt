package dev.slne.clan.velocity.extensions

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.VelocityClanPlugin

fun Player.findClan(clanService: ClanService): Clan? = clanService.findClanByMember(uniqueId)

@Suppress("UNCHECKED_CAST")
fun <C : ClanInvite> Player.findClanInvites(clanService: ClanService) =
    clanService.findInvitesByMember(uniqueId).map { it as C }

fun Clan.hasPermission(player: Player, permission: ClanPermission): Boolean {
    val clanMember = members.find { it.uuid == player.uniqueId } ?: return false

    return clanMember.hasPermission(permission)
}

val ClanMember.playerOrNull: Player?
    get() = VelocityClanPlugin.instance.server.getPlayer(uuid).orElse(null)

val ClanMember.player: Player
    get() = VelocityClanPlugin.instance.server.getPlayer(uuid)
        .orElseThrow { IllegalStateException("Player not found") }
