package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.velocity.commands.subcommands.options.clanTagColorCommand
import dev.slne.clan.velocity.permission.ClanPermissions

fun CommandAPICommand.clanOptionsCommand() = subcommand("options") {
    withPermission(ClanPermissions.CLAN_OPTIONS_COMMAND)

    clanTagColorCommand()
}