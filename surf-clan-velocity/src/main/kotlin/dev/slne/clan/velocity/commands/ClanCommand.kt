package dev.slne.clan.velocity.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.velocity.commands.subcommands.*
import dev.slne.clan.velocity.commands.subcommands.admin.ClanAdminCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanKickMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanMembersCommand
import dev.slne.clan.velocity.commands.subcommands.member.invite.ClanAcceptCommand
import dev.slne.clan.velocity.commands.subcommands.member.invite.ClanDenyCommand
import dev.slne.clan.velocity.commands.subcommands.member.invite.ClanInviteCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanDemoteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanPromoteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.player.ClanPlayerCommand

class ClanCommand : CommandAPICommand("clan") {
    init {
        withSubcommand(ClanCreateCommand())
        withSubcommand(ClanDisbandCommand())
        withSubcommand(ClanLeaveCommand())
        withSubcommand(ClanInfoCommand())
        withSubcommand(ClanSetDiscordCommand())

        withSubcommand(ClanInviteCommand())
        withSubcommands(ClanAcceptCommand())
        withSubcommands(ClanDenyCommand())

        withSubcommand(ClanPromoteMemberCommand())
        withSubcommand(ClanDemoteMemberCommand())
        withSubcommand(ClanKickMemberCommand())
        withSubcommand(ClanMembersCommand())

        withSubcommand(ClanPlayerCommand())
        withSubcommand(ClanAdminCommand())

        withSubcommand(ClanOptionsCommand())
    }
}