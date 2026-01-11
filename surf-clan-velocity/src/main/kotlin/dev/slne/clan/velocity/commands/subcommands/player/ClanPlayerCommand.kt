package dev.slne.clan.velocity.commands.subcommands.player

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.velocity.commands.subcommands.player.subcommands.clanPlayerSettingsCommand
import dev.slne.clan.velocity.permission.ClanPermissions

fun CommandAPICommand.clanPlayerCommand() = subcommand("player") {
    withPermission(ClanPermissions.CLAN_PLAYER_COMMAND)

    clanPlayerSettingsCommand()
}