package dev.slne.clan.velocity.commands.subcommands.member.invite

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.arguments
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.clan.velocity.redis.event.BroadcastMessageEvent
import dev.slne.surf.surfapi.core.api.command.args.awaiting
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend

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
                appendPrefix()
                success("Du hast die Einladung abgelehnt.")
            }

            BroadcastMessageEvent.broadcast(setOf(invite.invitedBy)) {
                variableValue(player.username)
                info(" hat deine Clan-Einladung abgelehnt.")
            }.await()
        }
    }
}