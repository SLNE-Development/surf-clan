package dev.slne.clan.velocity.commands.subcommands.admin

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.commands.subcommands.admin.subcommands.ClanRefreshCommand

class ClanAdminCommand(clanService: ClanService) : CommandAPICommand("admin") {
    init {
        withPermission("surf.clan.admin")

        withSubcommand(ClanRefreshCommand(clanService))
    }
}