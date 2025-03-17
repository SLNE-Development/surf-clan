package dev.slne.clan.velocity.commands.subcommands.player

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.commands.subcommands.player.subcommands.ClanPlayerSettingsCommand

class ClanPlayerCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("player") {
    init {
        withPermission("surf.clan.player")

        withSubcommand(ClanPlayerSettingsCommand(clanService, clanPlayerService))
    }
}