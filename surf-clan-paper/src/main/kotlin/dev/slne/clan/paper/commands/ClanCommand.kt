package dev.slne.clan.paper.commands

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.slne.clan.paper.commands.subcommands.*
import dev.slne.clan.paper.commands.subcommands.admin.clanAdminCommand
import dev.slne.clan.paper.commands.subcommands.member.clanKickMemberCommand
import dev.slne.clan.paper.commands.subcommands.member.clanMembersCommand
import dev.slne.clan.paper.commands.subcommands.member.invite.clanAcceptCommand
import dev.slne.clan.paper.commands.subcommands.member.invite.clanDenyCommand
import dev.slne.clan.paper.commands.subcommands.member.invite.clanInviteCommand
import dev.slne.clan.paper.commands.subcommands.member.role.clanDemoteMemberCommand
import dev.slne.clan.paper.commands.subcommands.member.role.clanPromoteMemberCommand
import dev.slne.clan.paper.commands.subcommands.player.clanPlayerCommand
import dev.slne.clan.paper.permission.ClanPermissions

fun clanCommand() = commandAPICommand("clan") {
    withPermission(ClanPermissions.CLAN_COMMAND)

    clanCreateCommand()
    clanDisbandCommand()
    clanLeaveCommand()
    clanInfoCommand()
    clanSetDiscordCommand()

    clanInviteCommand()
    clanAcceptCommand()
    clanDenyCommand()

    clanPromoteMemberCommand()
    clanDemoteMemberCommand()
    clanKickMemberCommand()
    clanMembersCommand()

    clanPlayerCommand()
    clanAdminCommand()

    clanOptionsCommand()
    clanListCommand()
}