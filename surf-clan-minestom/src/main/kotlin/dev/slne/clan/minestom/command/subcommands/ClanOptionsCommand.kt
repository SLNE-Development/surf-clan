package dev.slne.clan.minestom.command.subcommands

import dev.slne.clan.minestom.command.subcommands.options.clanTagColorCommand
import dev.slne.surf.api.minestom.command.CommandAPICommand
import dev.slne.surf.api.minestom.command.dsl.subcommand
import dev.slne.surf.clan.core.client.permission.ClanPermissions

fun CommandAPICommand.clanOptionsCommand(): CommandAPICommand = withSubcommand(
    subcommand("options") {
        withPermission(ClanPermissions.CLAN_OPTIONS_COMMAND)

        clanTagColorCommand()
    }
)
