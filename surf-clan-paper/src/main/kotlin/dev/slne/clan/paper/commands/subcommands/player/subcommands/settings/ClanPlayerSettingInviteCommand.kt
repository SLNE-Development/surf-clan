package dev.slne.clan.paper.commands.subcommands.player.subcommands.settings

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.booleanArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.client.command.NOTHING_CHANGED
import dev.slne.surf.clan.core.client.command.clanInvitesToggledMessage

fun CommandAPICommand.clanPlayerSettingInviteCommand() = subcommand("invite") {
    withPermission(ClanPermissions.CLAN_PLAYER_SETTINGS_INVITE_COMMAND)

    booleanArgument("accept", optional = true)

    playerExecutorSuspend { player, args ->
        val acceptByArgs = args.getUnchecked<Boolean?>("accept")
        val clanPlayer = ClanPlayer.byUuid(player.uniqueId)

        val accept = acceptByArgs ?: clanPlayer.acceptsClanInvites.not()
        val changed = clanPlayer.setAcceptsClanInvites(accept)

        if (!changed) {
            throw CommandAPI.failWithString(NOTHING_CHANGED)
        }

        player.sendMessage(clanInvitesToggledMessage(accept))
    }
}
