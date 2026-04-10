package dev.slne.clan.paper.commands.subcommands

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.optionalArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaitingOrNull
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components


fun CommandAPICommand.clanInfoCommand() = subcommand("info") {
    withPermission(ClanPermissions.CLAN_INFO_COMMAND)

    optionalArgument(ClanByClanTagArgument("clanTag"))

    playerExecutorSuspend { player, args ->
        val clan = args.awaitingOrNull<Clan>("clanTag") ?: Clan.byPlayer(player.uniqueId)

        if (clan == null) {
            throw CommandAPI.failWithString("Du bist in keinem Clan.")
        }

        player.sendMessage(Components.Clan.renderClanInformation(clan as ClanImpl))
    }
}