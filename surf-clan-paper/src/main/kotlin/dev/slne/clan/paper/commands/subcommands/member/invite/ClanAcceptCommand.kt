package dev.slne.clan.paper.commands.subcommands.member.invite

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteAcceptResult
import dev.slne.clan.paper.commands.arguments.ClanInviteArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components

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
                    appendSuccessPrefix()
                    success("Du bist dem Clan ")
                    append(Components.Clan.renderClanInformationHover(clan))
                    success(" beigetreten.")
                }

                clan.broadcast(buildText {
                    appendInfoPrefix()
                    variableValue(player.name)
                    info(" ist dem Clan beigetreten.")
                })
            }
        }
    }
}