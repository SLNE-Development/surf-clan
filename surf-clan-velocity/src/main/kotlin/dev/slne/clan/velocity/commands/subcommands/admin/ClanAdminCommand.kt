package dev.slne.clan.velocity.commands.subcommands.admin

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.velocity.commands.subcommands.admin.subcommands.ClanRefreshCommand

object ClanAdminCommand : CommandAPICommand("admin") {
    init {
        withPermission("surf.clan.admin")

        withSubcommand(ClanRefreshCommand)
    }
}