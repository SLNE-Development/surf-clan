package dev.slne.clan.velocity.commands.subcommands.player

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.velocity.commands.subcommands.player.subcommands.ClanPlayerSettingsCommand

class ClanPlayerCommand : CommandAPICommand("player") {
    init {
        withPermission("surf.clan.player")

        withSubcommand(ClanPlayerSettingsCommand())
    }
}