package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.components.Components
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

const val CLAN_MAX_MEMBERS_DISBAND = 50

/**
 * Why a clan may not be disbanded by a specific member.
 */
enum class DisbandError(val message: String) {
    NO_PERMISSION("Du hast keine Berechtigung, den Clan aufzulösen."),
    TOO_MANY_MEMBERS("Du kannst den Clan nicht auflösen, da er mehr als $CLAN_MAX_MEMBERS_DISBAND Mitglieder hat.")
}

/**
 * The reason [playerUuid] may not disband this clan, or `null` when they may.
 */
fun Clan.canBeDisbandedBy(playerUuid: UUID): DisbandError? {
    if (!hasMemberPermission(playerUuid, ClanPermission.DISBAND)) {
        return DisbandError.NO_PERMISSION
    }
    if (members.size > CLAN_MAX_MEMBERS_DISBAND) {
        return DisbandError.TOO_MANY_MEMBERS
    }
    return null
}

/**
 * Asks for confirmation before the clan is disbanded, with [click] carrying out the disband.
 */
suspend fun disbandConfirmationMessage(clan: Clan, click: ClickEvent<*>) = buildText {
    appendWarningPrefix()
    warning("Bist du dir sicher, dass du den Clan ")
    append(Components.Clan.renderClanInformationHover(clan as ClanImpl))
    warning(" auflösen möchtest?")
    appendSpace()
    warning("Klicke ")

    append {
        error("HIER", TextDecoration.BOLD)
        hoverEvent(disbandHoverText())
        clickEvent(click)
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

/**
 * Disbands the clan [clicker] confirmed, provided they are still in it and still allowed to.
 */
suspend fun handleDisbandClick(clicked: Audience, clicker: UUID, oldClanUuid: UUID) {
    val currentClan = Clan.byPlayer(clicker) ?: return clicked.sendText {
        appendErrorPrefix()
        error(Messages.NO_LONGER_IN_A_CLAN)
    }

    if (currentClan.uuid != oldClanUuid) return clicked.sendText {
        appendErrorPrefix()
        error("Du bist nicht mehr in dem Clan, den du auflösen wolltest.")
    }

    currentClan.canBeDisbandedBy(clicker)?.let { error ->
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
