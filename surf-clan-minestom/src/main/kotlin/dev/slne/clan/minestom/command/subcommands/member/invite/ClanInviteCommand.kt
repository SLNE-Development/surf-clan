package dev.slne.clan.minestom.command.subcommands.member.invite

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.*
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.minestom.command.argument.surfOfflinePlayerArgument
import kotlinx.coroutines.Deferred

fun CommandAPICommand.clanInviteCommand(): CommandAPICommand = withSubcommand(
    subcommand("invite") {
        withPermission(ClanPermissions.CLAN_INVITE_COMMAND)

        surfOfflinePlayerArgument("invitee")

        playerExecutorSuspend { player, args ->
            val invitee = args.get<Deferred<SurfPlayer?>>("invitee").await()
                ?: CommandAPI.failWithString(Messages.PLAYER_NOT_FOUND)
            val clan = Clan.byPlayer(player.uuid)
                ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

            if (!clan.hasMemberPermission(player.uuid, ClanPermission.INVITE)) {
                CommandAPI.failWithString(NO_INVITE_PERMISSION)
            }

            when (clan.invite(invitee.uuid, player.uuid)) {
                ClanInviteResult.AlreadyInClan -> CommandAPI.failWithString(INVITEE_ALREADY_IN_CLAN)
                ClanInviteResult.AlreadyInvited -> CommandAPI.failWithString(INVITEE_ALREADY_INVITED)
                ClanInviteResult.InvitationsDisabled -> CommandAPI.failWithString(
                    INVITEE_DISABLED_INVITATIONS
                )

                is ClanInviteResult.Success -> {
                    val inviteeName = invitee.lastKnownName ?: invitee.uuid.toString()

                    player.sendMessage(inviteSentMessage(inviteeName, clan as ClanImpl))

                    SurfCoreApi.getPlayer(invitee.uuid)?.let { inviteeTarget ->
                        SurfCoreApi.sendText(
                            inviteeTarget,
                            inviteReceivedMessage(player.username, clan.name)
                        )
                    }
                }
            }
        }
    }
)
