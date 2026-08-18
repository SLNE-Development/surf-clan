package dev.slne.clan.minestom.command.subcommands

import dev.slne.clan.minestom.command.subcommands.options.clanTagColorCommand
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.client.permission.ClanPermissions

fun CommandAPICommand.clanOptionsCommand(): CommandAPICommand = withSubcommand(
    subcommand("options") {
        withPermission(ClanPermissions.CLAN_OPTIONS_COMMAND)

        clanTagColorCommand()
    }
)
