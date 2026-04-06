package dev.slne.clan.paper.commands.subcommands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.clan.paper.plugin
import dev.slne.surf.api.core.messages.adventure.appendNewline
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.toJavaDuration

private const val CLAN_MAX_MEMBERS_DISBAND = 50

fun CommandAPICommand.clanDisbandCommand() = subcommand("disband") {
    withPermission(ClanPermissions.CLAN_DISBAND_COMMAND)

    playerExecutorSuspend { player, args ->
        val playerUuid = player.uniqueId
        val clan =
            Clan.byPlayer(playerUuid) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        clan.canBeDisbandedBy(playerUuid)?.let { error ->
            throw CommandAPI.failWithString(error.message)
        }

        player.sendDisbandConfirmation(clan)
    }
}

private suspend fun Player.sendDisbandConfirmation(clan: Clan) = sendText {
    appendWarningPrefix()
    warning("Bist du dir sicher, dass du den Clan ")
    append(Components.Clan.renderClanInformationHover(clan as ClanImpl))
    warning(" auflösen möchtest?")
    appendSpace()
    warning("Klicke ")

    append {
        error("HIER", TextDecoration.BOLD)
        hoverEvent(disbandHoverText())
        clickEvent(createDisbandClickEvent(clan.uuid))
    }

    warning(" um den Clan aufzulösen.")
}

private fun disbandHoverText() = buildText {
    info("Klicke hier, um den Clan aufzulösen.")
    appendNewline(2)
    error("Achtung: ", TextDecoration.BOLD)
    error("Alle Daten des Clans werden gelöscht ")
    appendNewline()
    error("und können nicht wiederhergestellt werden.")
    appendNewline()
    error("Auch der Support kann keine Daten wiederherstellen.")
}

private fun createDisbandClickEvent(oldClanUuid: UUID): ClickEvent = ClickEvent.callback(
    ClickCallback.widen({ clicked ->
        plugin.launch {
            handleDisbandClick(clicked, oldClanUuid)
        }
    }, Player::class.java)
) { it.lifetime(1.minutes.toJavaDuration()) }

private suspend fun handleDisbandClick(clicked: Player, oldClanUuid: UUID) {
    val uuid = clicked.uniqueId

    val currentClan = Clan.byPlayer(uuid) ?: return clicked.sendText {
        appendErrorPrefix()
        error("Du bist nicht mehr in einem Clan.")
    }

    if (currentClan.uuid != oldClanUuid) return clicked.sendText {
        appendErrorPrefix()
        error("Du bist nicht mehr in dem Clan, den du auflösen wolltest.")
    }

    currentClan.canBeDisbandedBy(uuid)?.let { error ->
        return clicked.sendText {
            appendErrorPrefix()
            error(error.message)
        }
    }

    if (!currentClan.delete()) return clicked.sendText {
        appendErrorPrefix()
        error("Beim Löschen des Clans ist ein Fehler aufgetreten.")
    }

    broadcastClanDisband(currentClan)
}

private suspend fun broadcastClanDisband(deletedClan: Clan) {
    val message = buildText {
        appendSuccessPrefix()
        success("Der Clan ")
        append(Components.Clan.renderClanInformationHover(deletedClan as ClanImpl))
        success(" wurde erfolgreich aufgelöst.")
    }

    deletedClan.broadcast(message)
}

private fun Clan.canBeDisbandedBy(playerUuid: UUID): DisbandError? {
    if (!hasMemberPermission(playerUuid, ClanPermission.DISBAND)) {
        return DisbandError.NO_PERMISSION
    }
    if (members.size > CLAN_MAX_MEMBERS_DISBAND) {
        return DisbandError.TOO_MANY_MEMBERS
    }
    return null
}

private enum class DisbandError(val message: String) {
    NO_PERMISSION("Du hast keine Berechtigung, den Clan aufzulösen."),
    TOO_MANY_MEMBERS("Du kannst den Clan nicht auflösen, da er mehr als $CLAN_MAX_MEMBERS_DISBAND Mitglieder hat.")
}
