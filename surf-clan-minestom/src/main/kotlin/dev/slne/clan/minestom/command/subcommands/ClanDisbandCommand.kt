package dev.slne.clan.minestom.command.subcommands

import dev.slne.clan.api.clan.Clan
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.minestom.lobby.api.coroutine.minestomAsyncScope
import dev.slne.surf.api.core.messages.adventure.lifetime
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.canBeDisbandedBy
import dev.slne.surf.clan.core.client.command.disbandConfirmationMessage
import dev.slne.surf.clan.core.client.command.handleDisbandClick
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import kotlinx.coroutines.launch
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.minestom.server.entity.Player
import java.util.*
import kotlin.time.Duration.Companion.minutes

fun CommandAPICommand.clanDisbandCommand(): CommandAPICommand = withSubcommand(
    subcommand("disband") {
        withPermission(ClanPermissions.CLAN_DISBAND_COMMAND)

        playerExecutorSuspend { player, args ->
            val playerUuid = player.uuid
            val clan =
                Clan.byPlayer(playerUuid) ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

            clan.canBeDisbandedBy(playerUuid)?.let { error ->
                CommandAPI.failWithString(error.message)
            }

            player.sendMessage(disbandConfirmationMessage(clan, createDisbandClickEvent(clan.uuid)))
        }
    }
)

private fun createDisbandClickEvent(oldClanUuid: UUID): ClickEvent<*> = ClickEvent.callback(
    ClickCallback.widen({ clicked ->
        minestomAsyncScope.launch {
            handleDisbandClick(clicked, clicked.uuid, oldClanUuid)
        }
    }, Player::class.java)
) { it.lifetime(1.minutes) }
