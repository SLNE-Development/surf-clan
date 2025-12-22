package dev.slne.clan.velocity.extensions

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.api.Clan
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.service.clanService
import dev.slne.clan.velocity.plugin
import java.util.*

fun Player.findClan(): Clan? = clanService.findClanByMember(uniqueId)
fun Player.findClanInvites() = clanService.findInvitesByMember(uniqueId)
fun Clan.hasPermission(player: Player, permission: ClanPermission) =
    members.find { it.uuid == player.uniqueId }?.hasPermission(permission) ?: false

val ClanMember.playerOrNull: Player? get() = plugin.server.getPlayer(uuid).orElse(null)
val UUID.playerOrNull: Player? get() = plugin.server.getPlayer(this).orElse(null)

val ClanMember.player: Player
    get() = plugin.server.getPlayer(uuid).orElseThrow { IllegalStateException("Player not found") }
