package dev.slne.clan.paper.commands.subcommands.member.invite

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.arguments
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.commands.arguments.OfflinePlayerArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.*
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.player.SurfPlayer

fun CommandAPICommand.clanInviteCommand() = subcommand("invite") {
    withPermission(ClanPermissions.CLAN_INVITE_COMMAND)

    arguments(OfflinePlayerArgument("invitee"))

    playerExecutorSuspend { player, args ->
        val invitee = args.awaiting<SurfPlayer>("invitee")
        val clan = Clan.byPlayer(player.uniqueId)
            ?: throw CommandAPI.failWithString(Messages.NOT_IN_CLAN)

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.INVITE)) {
            throw CommandAPI.failWithString(NO_INVITE_PERMISSION)
        }

        when (clan.invite(invitee.uuid, player.uniqueId)) {
            ClanInviteResult.AlreadyInClan -> throw CommandAPI.failWithString(INVITEE_ALREADY_IN_CLAN)
            ClanInviteResult.AlreadyInvited -> throw CommandAPI.failWithString(INVITEE_ALREADY_INVITED)
            ClanInviteResult.InvitationsDisabled -> throw CommandAPI.failWithString(
                INVITEE_DISABLED_INVITATIONS
            )

            is ClanInviteResult.Success -> {
                val inviteeName = invitee.lastKnownName ?: invitee.uuid.toString()

                player.sendMessage(inviteSentMessage(inviteeName, clan as ClanImpl))

                SurfCoreApi.getPlayer(invitee.uuid)?.let { inviteeTarget ->
                    SurfCoreApi.sendText(
                        inviteeTarget,
                        inviteReceivedMessage(player.name, clan.name)
                    )
                }
            }
        }
    }
}
