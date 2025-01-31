package dev.slne.clan.velocity.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.commands.subcommands.ClanCreateCommand
import dev.slne.clan.velocity.commands.subcommands.ClanDisbandCommand
import dev.slne.clan.velocity.commands.subcommands.ClanInfoCommand
import dev.slne.clan.velocity.commands.subcommands.ClanLeaveCommand
import dev.slne.clan.velocity.commands.subcommands.admin.ClanAdminCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanKickMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanMembersCommand
import dev.slne.clan.velocity.commands.subcommands.member.invite.ClanInviteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanDemoteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanPromoteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.player.ClanPlayerCommand

class ClanCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("clan") {
    init {
        withSubcommand(ClanCreateCommand(clanService, clanPlayerService))
        withSubcommand(ClanDisbandCommand(clanService, clanPlayerService))
        withSubcommand(ClanLeaveCommand(clanService, clanPlayerService))
        withSubcommand(ClanInfoCommand(clanService, clanPlayerService))

        withSubcommand(ClanInviteMemberCommand(clanService, clanPlayerService))
        withSubcommand(ClanPromoteMemberCommand(clanService, clanPlayerService))
        withSubcommand(ClanDemoteMemberCommand(clanService, clanPlayerService))
        withSubcommand(ClanKickMemberCommand(clanService, clanPlayerService))
        withSubcommand(ClanMembersCommand(clanService, clanPlayerService))

        withSubcommand(ClanPlayerCommand(clanService, clanPlayerService))
        withSubcommand(ClanAdminCommand(clanService))
    }
}