package dev.slne.clan.velocity.commands.subcommands.player.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.velocity.commands.subcommands.player.subcommands.settings.ClanPlayerSettingInviteCommand

class ClanPlayerSettingsCommand : CommandAPICommand("settings") {
    init {
        withPermission("surf.clan.player.settings")

        withSubcommand(ClanPlayerSettingInviteCommand())
    }
}