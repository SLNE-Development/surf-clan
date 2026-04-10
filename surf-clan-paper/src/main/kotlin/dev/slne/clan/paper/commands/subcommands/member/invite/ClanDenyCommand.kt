package dev.slne.clan.paper.commands.subcommands.member.invite

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.arguments
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.paper.commands.arguments.ClanInviteArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.util.sendText

fun CommandAPICommand.clanDenyCommand() = subcommand("deny") {
    withPermission(ClanPermissions.CLAN_DENY_INVITE_COMMAND)

    arguments(ClanInviteArgument("invite"))

    playerExecutorSuspend { player, args ->
        val invite = args.awaiting<ClanInvite>("invite")

        val revoked = invite.revoke()

        if (!revoked) {
            throw CommandAPI.failWithString("Diese Einladung wurde bereits abgelehnt.")
        } else {
            player.sendText {
                appendSuccessPrefix()
                success("Du hast die Einladung abgelehnt.")
            }

            SurfCoreApi.getPlayer(invite.invitedBy)?.sendText {
                appendInfoPrefix()
                variableValue(player.name)
                info(" hat deine Clan-Einladung abgelehnt.")
            }
        }
    }
}