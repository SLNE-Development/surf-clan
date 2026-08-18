package dev.slne.clan.paper.commands.subcommands.member.invite

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.arguments
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.paper.commands.arguments.ClanInviteArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.client.command.INVITE_ALREADY_DENIED
import dev.slne.surf.clan.core.client.command.inviteDeniedMessage
import dev.slne.surf.clan.core.client.command.inviteDeniedNotification
import dev.slne.surf.core.api.common.SurfCoreApi

fun CommandAPICommand.clanDenyCommand() = subcommand("deny") {
    withPermission(ClanPermissions.CLAN_DENY_INVITE_COMMAND)

    arguments(ClanInviteArgument("invite"))

    playerExecutorSuspend { player, args ->
        val invite = args.awaiting<ClanInvite>("invite")

        val revoked = invite.revoke()

        if (!revoked) {
            throw CommandAPI.failWithString(INVITE_ALREADY_DENIED)
        } else {
            player.sendMessage(inviteDeniedMessage())

            SurfCoreApi.getPlayer(invite.invitedBy)?.let { inviter ->
                SurfCoreApi.sendText(inviter, inviteDeniedNotification(player.name))
            }
        }
    }
}
