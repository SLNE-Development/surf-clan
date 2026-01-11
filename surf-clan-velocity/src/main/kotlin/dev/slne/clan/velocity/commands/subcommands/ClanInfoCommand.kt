package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.optionalArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.components.Components
import dev.slne.clan.velocity.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.surf.surfapi.core.api.command.args.awaitingOrNull
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend


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