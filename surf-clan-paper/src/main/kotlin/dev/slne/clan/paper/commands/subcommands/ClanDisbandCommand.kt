package dev.slne.clan.paper.commands.subcommands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.clan.paper.plugin
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.canBeDisbandedBy
import dev.slne.surf.clan.core.client.command.disbandConfirmationMessage
import dev.slne.surf.clan.core.client.command.handleDisbandClick
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.entity.Player
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

fun CommandAPICommand.clanDisbandCommand() = subcommand("disband") {
    withPermission(ClanPermissions.CLAN_DISBAND_COMMAND)

    playerExecutorSuspend { player, args ->
        val playerUuid = player.uniqueId
        val clan =
            Clan.byPlayer(playerUuid) ?: throw CommandAPI.failWithString(Messages.NOT_IN_CLAN)

        clan.canBeDisbandedBy(playerUuid)?.let { error ->
            throw CommandAPI.failWithString(error.message)
        }

        player.sendMessage(disbandConfirmationMessage(clan, createDisbandClickEvent(clan.uuid)))
    }
}

private fun createDisbandClickEvent(oldClanUuid: UUID): ClickEvent<*> = ClickEvent.callback(
    ClickCallback.widen({ clicked ->
        plugin.launch {
            handleDisbandClick(clicked, clicked.uniqueId, oldClanUuid)
        }
    }, Player::class.java)
) { it.lifetime(1.minutes.toJavaDuration()) }
