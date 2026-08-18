package dev.slne.clan.paper.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaitingOrNull
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.whoisMessage
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.paper.command.argument.surfOfflinePlayerArgument

fun CommandAPICommand.clanWhoisCommand() = subcommand("whois") {
    withPermission(ClanPermissions.CLAN_WHOIS_COMMAND)
    surfOfflinePlayerArgument("target")
    playerExecutorSuspend { player, args ->
        val target = args.awaitingOrNull<SurfPlayer?>("target")

        if (target == null) {
            player.sendText {
                appendErrorPrefix()
                error(Messages.PLAYER_NOT_FOUND)
            }
            return@playerExecutorSuspend
        }

        val targetName = target.lastKnownName ?: target.uuid.toString()
        player.sendMessage(whoisMessage(targetName, Clan.byPlayer(target.uuid)))
    }
}
