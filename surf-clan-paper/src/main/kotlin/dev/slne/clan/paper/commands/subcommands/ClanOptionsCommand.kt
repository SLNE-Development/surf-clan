package dev.slne.clan.paper.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.paper.commands.subcommands.options.clanTagColorCommand
import dev.slne.clan.paper.permission.ClanPermissions

fun CommandAPICommand.clanOptionsCommand() = subcommand("options") {
    withPermission(ClanPermissions.CLAN_OPTIONS_COMMAND)

    clanTagColorCommand()
}