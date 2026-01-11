package dev.slne.clan.velocity.commands.subcommands.member.invite

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteAcceptResult
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.components.Components
import dev.slne.clan.velocity.commands.arguments.ClanInviteArgument
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.clan.velocity.redis.event.ClanBroadcastRedisEvent
import dev.slne.surf.surfapi.core.api.command.args.awaiting
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend

fun CommandAPICommand.clanAcceptCommand() = subcommand("accept") {
    withPermission(ClanPermissions.CLAN_ACCEPT_INVITE_COMMAND)

    argument(ClanInviteArgument("invite"))
    playerExecutorSuspend { player, args ->
        val invite = args.awaiting<ClanInvite>("invite")

        when (val result = invite.accept()) {
            ClanInviteAcceptResult.AlreadyInClan -> throw CommandAPI.failWithString("Du bist bereits in einem Clan.")
            is ClanInviteAcceptResult.Accepted -> {
                val clan = result.clan as ClanImpl

                player.sendText {
                    appendPrefix()
                    success("Du bist dem Clan ")
                    append(Components.Clan.renderClanInformationHover(clan))
                    success(" beigetreten.")
                }

                ClanBroadcastRedisEvent.broadcast(clan) {
                    appendPrefix()
                    variableValue(player.username)
                    info(" ist dem Clan beigetreten.")
                }.await()
            }
        }
    }
}