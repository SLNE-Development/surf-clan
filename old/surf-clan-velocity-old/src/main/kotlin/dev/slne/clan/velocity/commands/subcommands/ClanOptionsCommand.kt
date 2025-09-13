package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.commands.subcommands.options.ClanTagOptionTagColorCommand

class ClanOptionsCommand(
    clanService: ClanService
) : CommandAPICommand("options") {
    init {
        withPermission("surf.clan.options")
        
        subcommand(ClanTagOptionTagColorCommand(clanService))
    }
}