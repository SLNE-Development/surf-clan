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
import dev.slne.surf.clan.core.client.command.CLAN_OWNER_CANNOT_LEAVE
import dev.slne.surf.clan.core.client.command.handleLeaveClick
import dev.slne.surf.clan.core.client.command.leaveConfirmationMessage
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import org.bukkit.entity.Player
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

fun CommandAPICommand.clanLeaveCommand() = subcommand("leave") {
    withPermission(ClanPermissions.CLAN_LEAVE_COMMAND)

    playerExecutorSuspend { player, args ->
        val clan = Clan.byPlayer(player.uniqueId)
            ?: throw CommandAPI.failWithString(Messages.NOT_IN_CLAN)

        if (clan.createdByUuid == player.uniqueId) {
            throw CommandAPI.failWithString(CLAN_OWNER_CANNOT_LEAVE)
        }

        player.sendMessage(leaveConfirmationMessage(clan, createConfirmCallback(clan.uuid)))
    }
}

private fun createConfirmCallback(originalClanUuid: UUID) = ClickEvent.callback(
    ClickCallback.widen({ clicked ->
        plugin.launch {
            handleLeaveClick(clicked, clicked.uniqueId, clicked.name, originalClanUuid)
        }
    }, Player::class.java)
) { it.lifetime(1.minutes.toJavaDuration()) }
