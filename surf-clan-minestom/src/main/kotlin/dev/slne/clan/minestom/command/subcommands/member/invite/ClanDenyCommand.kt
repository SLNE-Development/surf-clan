package dev.slne.clan.minestom.command.subcommands.member.invite

import dev.slne.clan.minestom.command.arguments.clanInviteArgument
import dev.slne.clan.minestom.command.resolveClanInvite
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.client.command.INVITE_ALREADY_DENIED
import dev.slne.surf.clan.core.client.command.inviteDeniedMessage
import dev.slne.surf.clan.core.client.command.inviteDeniedNotification
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import dev.slne.surf.core.api.common.SurfCoreApi

fun CommandAPICommand.clanDenyCommand(): CommandAPICommand = withSubcommand(
    subcommand("deny") {
        withPermission(ClanPermissions.CLAN_DENY_INVITE_COMMAND)

        clanInviteArgument("invite")

        playerExecutorSuspend { player, args ->
            val invite = args.resolveClanInvite("invite")

            val revoked = invite.revoke()

            if (!revoked) {
                CommandAPI.failWithString(INVITE_ALREADY_DENIED)
            } else {
                player.sendMessage(inviteDeniedMessage())

                SurfCoreApi.getPlayer(invite.invitedBy)?.let { inviter ->
                    SurfCoreApi.sendText(inviter, inviteDeniedNotification(player.username))
                }
            }
        }
    }
)
