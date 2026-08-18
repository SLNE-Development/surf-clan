package dev.slne.clan.minestom.command.subcommands.member.invite

import dev.slne.clan.api.invite.ClanInviteAcceptResult
import dev.slne.clan.minestom.command.arguments.clanInviteArgument
import dev.slne.clan.minestom.command.resolveClanInvite
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.inviteAcceptedMessage
import dev.slne.surf.clan.core.client.command.memberJoinedMessage
import dev.slne.surf.clan.core.client.permission.ClanPermissions

fun CommandAPICommand.clanAcceptCommand(): CommandAPICommand = withSubcommand(
    subcommand("accept") {
        withPermission(ClanPermissions.CLAN_ACCEPT_INVITE_COMMAND)

        clanInviteArgument("invite")

        playerExecutorSuspend { player, args ->
            val invite = args.resolveClanInvite("invite")

            when (val result = invite.accept()) {
                ClanInviteAcceptResult.AlreadyInClan -> CommandAPI.failWithString(Messages.ALREADY_IN_CLAN)
                is ClanInviteAcceptResult.Accepted -> {
                    val clan = result.clan as ClanImpl

                    player.sendMessage(inviteAcceptedMessage(clan))
                    clan.broadcast(memberJoinedMessage(player.username))
                }
            }
        }
    }
)
