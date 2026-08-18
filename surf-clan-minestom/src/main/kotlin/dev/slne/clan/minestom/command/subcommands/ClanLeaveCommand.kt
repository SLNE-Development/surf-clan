package dev.slne.clan.minestom.command.subcommands

import dev.slne.clan.api.clan.Clan
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.minestom.lobby.api.coroutine.minestomAsyncScope
import dev.slne.surf.api.core.messages.adventure.lifetime
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.CLAN_OWNER_CANNOT_LEAVE
import dev.slne.surf.clan.core.client.command.handleLeaveClick
import dev.slne.surf.clan.core.client.command.leaveConfirmationMessage
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import kotlinx.coroutines.launch
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.minestom.server.entity.Player
import java.util.*
import kotlin.time.Duration.Companion.minutes

fun CommandAPICommand.clanLeaveCommand(): CommandAPICommand = withSubcommand(
    subcommand("leave") {
        withPermission(ClanPermissions.CLAN_LEAVE_COMMAND)

        playerExecutorSuspend { player, args ->
            val clan = Clan.byPlayer(player.uuid)
                ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

            if (clan.createdByUuid == player.uuid) {
                CommandAPI.failWithString(CLAN_OWNER_CANNOT_LEAVE)
            }

            player.sendMessage(leaveConfirmationMessage(clan, createConfirmCallback(clan.uuid)))
        }
    }
)

private fun createConfirmCallback(originalClanUuid: UUID) = ClickEvent.callback(
    ClickCallback.widen({ clicked ->
        minestomAsyncScope.launch {
            handleLeaveClick(clicked, clicked.uuid, clicked.username, originalClanUuid)
        }
    }, Player::class.java)
) { it.lifetime(1.minutes) }
