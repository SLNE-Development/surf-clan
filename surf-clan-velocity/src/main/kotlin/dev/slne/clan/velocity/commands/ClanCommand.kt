package dev.slne.clan.velocity.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.commands.subcommands.ClanCreateCommand
import dev.slne.clan.velocity.commands.subcommands.ClanDisbandCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanKickMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.invite.ClanInviteCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanDemoteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanPromoteMemberCommand

class ClanCommand(clanService: ClanService) : CommandAPICommand("clan") {
    init {
        withSubcommands(ClanCreateCommand(clanService))
        withSubcommand(ClanInviteCommand(clanService))
        withSubcommand(ClanPromoteMemberCommand(clanService))
        withSubcommand(ClanDemoteMemberCommand(clanService))
        withSubcommand(ClanDisbandCommand(clanService))
        withSubcommand(ClanKickMemberCommand(clanService))
    }
}