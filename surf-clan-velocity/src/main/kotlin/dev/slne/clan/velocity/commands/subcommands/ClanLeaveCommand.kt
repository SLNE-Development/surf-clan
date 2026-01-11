package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.components.Components
import dev.slne.clan.core.redis.RedisService
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.redis.event.BroadcastMessageEvent
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

fun CommandAPICommand.clanLeaveCommand() = subcommand("leave") {
    withPermission(ClanPermissions.CLAN_LEAVE_COMMAND)

    playerExecutorSuspend { player, args ->
        val clan = Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        if (clan.createdByUuid == player.uniqueId) {
            throw CommandAPI.failWithString("Du bist der Besitzer des Clans und kannst ihn nicht verlassen. Nutze /clan disband um den Clan aufzulösen.")
        }

        player.sendText {
            warning("Möchtest du den Clan ")
            append(Components.Clan.renderClanInformationHover(clan as ClanImpl))
            warning(" wirklich verlassen? Klicke ")
            append {
                error("HIER", TextDecoration.BOLD)
                hoverEvent(createHoverEvent())
                clickEvent(createConfirmCallback(clan.uuid))
            }
            warning(" um den Clan zu verlassen.")
        }
    }
}

private fun createHoverEvent() = buildText {
    info("Klicke hier um den Clan zu verlassen.")
    appendNewline(3)
    error("Achtung: ", TextDecoration.BOLD)
    error("Du kannst den Vorgang nicht rückgängig machen.")
    appendNewline(2)
    error("Wenn du den Clan verlässt, verlierst du alle Rechte")
    appendNewline()
    error("und benötigst erneut eine Einladung, um wieder den Clan beitreten zu können.")
}

private fun createConfirmCallback(originalClanUuid: UUID) = ClickEvent.callback(
    ClickCallback.widen({ clicked ->
        plugin.container.launch {
            handleLeaveClick(clicked, originalClanUuid)
        }
    }, Player::class.java)
) { it.lifetime(1.minutes.toJavaDuration()) }

private suspend fun handleLeaveClick(clicked: Player, originalClanUuid: UUID) {
    val clan = Clan.byPlayer(clicked.uniqueId) ?: return clicked.sendText {
        appendPrefix()
        error("Du bist nicht mehr in einem Clan.")
    }

    if (clan.uuid != originalClanUuid) return clicked.sendText {
        appendPrefix()
        error("Der Clan, den du verlassen wolltest, hat sich geändert. Bitte versuche es erneut.")
    }

    val removed = clan.removeMember(clicked.uniqueId)

    if (!removed) {
        return clicked.sendText {
            appendPrefix()
            error("Du bist nicht mehr in dem Clan.")
        }
    } else {
        clicked.sendText {
            appendPrefix()
            success("Du hast den Clan verlassen.")
        }

        val memberLeftMessage = buildText {
            variableValue(clicked.username)
            info(" hat den Clan verlassen.")
        }

        val memberUuids = clan.members.map { it.uuid }.toSet()
        RedisService.publish(BroadcastMessageEvent(memberLeftMessage, memberUuids)).await()
    }
}