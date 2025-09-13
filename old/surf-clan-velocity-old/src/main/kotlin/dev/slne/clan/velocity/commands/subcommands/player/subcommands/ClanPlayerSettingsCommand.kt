package dev.slne.clan.velocity.commands.subcommands.player.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.commands.subcommands.player.subcommands.settings.ClanPlayerSettingInviteCommand

class ClanPlayerSettingsCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("settings") {
    init {
        withPermission("surf.clan.player.settings")

        withSubcommand(ClanPlayerSettingInviteCommand(clanService, clanPlayerService))
    }
}