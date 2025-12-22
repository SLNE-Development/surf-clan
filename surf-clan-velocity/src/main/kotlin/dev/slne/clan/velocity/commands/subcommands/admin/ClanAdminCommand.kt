package dev.slne.clan.velocity.commands.subcommands.admin

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.velocity.commands.subcommands.admin.subcommands.ClanRefreshCommand
import dev.slne.clan.velocity.commands.subcommands.admin.subcommands.ClanReloadCommand

class ClanAdminCommand : CommandAPICommand("admin") {
    init {
        withPermission("surf.clan.admin")
        withSubcommand(ClanRefreshCommand())
        withSubcommand(ClanReloadCommand())
    }
}