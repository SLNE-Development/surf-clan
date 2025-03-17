package dev.slne.clan.velocity.commands

import dev.jorel.commandapi.CommandAPICommand
import dev.slne.clan.velocity.commands.subcommands.*
import dev.slne.clan.velocity.commands.subcommands.admin.ClanAdminCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanKickMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.ClanMembersCommand
import dev.slne.clan.velocity.commands.subcommands.member.invite.ClanInviteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanDemoteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.member.role.ClanPromoteMemberCommand
import dev.slne.clan.velocity.commands.subcommands.player.ClanPlayerCommand

object ClanCommand : CommandAPICommand("clan") {
    init {
        withSubcommand(ClanCreateCommand)
        withSubcommand(ClanDisbandCommand)
        withSubcommand(ClanLeaveCommand)
        withSubcommand(ClanInfoCommand)
        withSubcommand(ClanSetDiscordCommand)

        withSubcommand(ClanInviteMemberCommand)
        withSubcommand(ClanPromoteMemberCommand)
        withSubcommand(ClanDemoteMemberCommand)
        withSubcommand(ClanKickMemberCommand)
        withSubcommand(ClanMembersCommand)

        withSubcommand(ClanPlayerCommand)
        withSubcommand(ClanAdminCommand)
    }
}