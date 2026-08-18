package dev.slne.clan.minestom.command

import dev.slne.clan.minestom.command.subcommands.*
import dev.slne.clan.minestom.command.subcommands.admin.clanAdminCommand
import dev.slne.clan.minestom.command.subcommands.member.clanKickMemberCommand
import dev.slne.clan.minestom.command.subcommands.member.clanMembersCommand
import dev.slne.clan.minestom.command.subcommands.member.invite.clanAcceptCommand
import dev.slne.clan.minestom.command.subcommands.member.invite.clanDenyCommand
import dev.slne.clan.minestom.command.subcommands.member.invite.clanInviteCommand
import dev.slne.clan.minestom.command.subcommands.member.role.clanDemoteMemberCommand
import dev.slne.clan.minestom.command.subcommands.member.role.clanPromoteMemberCommand
import dev.slne.clan.minestom.command.subcommands.player.clanPlayerCommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.commandAPICommand
import dev.slne.surf.clan.core.client.permission.ClanPermissions

fun clanCommand() = commandAPICommand("clan") {
    withPermission(ClanPermissions.CLAN_COMMAND)

    clanCreateCommand()
    clanDisbandCommand()
    clanLeaveCommand()
    clanInfoCommand()
    clanWhoisCommand()
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

    withSubcommand(clanChatCommand("chat"))
}
