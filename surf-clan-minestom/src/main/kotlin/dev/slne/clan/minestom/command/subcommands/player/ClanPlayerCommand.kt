package dev.slne.clan.minestom.command.subcommands.player

import dev.slne.clan.api.player.ClanPlayer
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.booleanArgument
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.client.command.NOTHING_CHANGED
import dev.slne.surf.clan.core.client.command.clanInvitesToggledMessage
import dev.slne.surf.clan.core.client.permission.ClanPermissions

fun CommandAPICommand.clanPlayerCommand(): CommandAPICommand = withSubcommand(
    subcommand("player") {
        withPermission(ClanPermissions.CLAN_PLAYER_COMMAND)

        clanPlayerSettingsCommand()
    }
)

private fun CommandAPICommand.clanPlayerSettingsCommand(): CommandAPICommand = withSubcommand(
    subcommand("settings") {
        withPermission(ClanPermissions.CLAN_PLAYER_SETTINGS_COMMAND)

        clanPlayerSettingInviteCommand()
    }
)

private fun CommandAPICommand.clanPlayerSettingInviteCommand(): CommandAPICommand = withSubcommand(
    subcommand("invite") {
        withPermission(ClanPermissions.CLAN_PLAYER_SETTINGS_INVITE_COMMAND)

        booleanArgument("accept", optional = true)

        playerExecutorSuspend { player, args ->
            val acceptByArgs = args.getOptional<Boolean>("accept")
            val clanPlayer = ClanPlayer.byUuid(player.uuid)

            val accept = acceptByArgs ?: clanPlayer.acceptsClanInvites.not()
            val changed = clanPlayer.setAcceptsClanInvites(accept)

            if (!changed) {
                CommandAPI.failWithString(NOTHING_CHANGED)
            }

            player.sendMessage(clanInvitesToggledMessage(accept))
        }
    }
)
