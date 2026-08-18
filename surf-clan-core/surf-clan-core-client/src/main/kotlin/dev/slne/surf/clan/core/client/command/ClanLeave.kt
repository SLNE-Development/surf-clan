package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.components.Components
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

const val CLAN_OWNER_CANNOT_LEAVE =
    "Du bist der Besitzer des Clans und kannst ihn nicht verlassen. Nutze /clan disband um den Clan aufzulösen."

/**
 * Asks for confirmation before the clan is left, with [click] carrying out the leave.
 */
suspend fun leaveConfirmationMessage(clan: Clan, click: ClickEvent<*>) = buildText {
    appendWarningPrefix()
    warning("Möchtest du den Clan ")
    append(Components.Clan.renderClanInformationHover(clan as ClanImpl))
    warning(" wirklich verlassen? Klicke ")
    append {
        error("HIER", TextDecoration.BOLD)
        hoverEvent(leaveHoverText())
        clickEvent(click)
    }
    warning(" um den Clan zu verlassen.")
}

private fun leaveHoverText() = buildText {
    info("Klicke hier um den Clan zu verlassen.")
    appendNewline(3)
    error("Achtung: ", TextDecoration.BOLD)
    error("Du kannst den Vorgang nicht rückgängig machen.")
    appendNewline(2)
    error("Wenn du den Clan verlässt, verlierst du alle Rechte")
    appendNewline()
    error("und benötigst erneut eine Einladung, um wieder den Clan beitreten zu können.")
}

/**
 * Removes [clicker] from the clan they confirmed leaving, provided they are still in that clan.
 */
suspend fun handleLeaveClick(
    clicked: Audience,
    clicker: UUID,
    clickerName: String,
    originalClanUuid: UUID
) {
    val clan = Clan.byPlayer(clicker) ?: return clicked.sendText {
        appendErrorPrefix()
        error(Messages.NO_LONGER_IN_A_CLAN)
    }

    if (clan.uuid != originalClanUuid) return clicked.sendText {
        appendErrorPrefix()
        error("Der Clan, den du verlassen wolltest, hat sich geändert. Bitte versuche es erneut.")
    }

    val removed = clan.removeMember(clicker)

    if (!removed) {
        return clicked.sendText {
            appendErrorPrefix()
            error("Du bist nicht mehr in dem Clan.")
        }
    } else {
        clicked.sendText {
            appendErrorPrefix()
            success("Du hast den Clan verlassen.")
        }

        val memberLeftMessage = buildText {
            appendInfoPrefix()
            variableValue(clickerName)
            info(" hat den Clan verlassen.")
        }

        clan.broadcast(memberLeftMessage)
    }
}
