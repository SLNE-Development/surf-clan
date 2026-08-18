package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.surf.api.core.messages.adventure.buildText
import net.kyori.adventure.text.event.ClickEvent

const val NO_TAG_COLOR_PERMISSION = "Du hast keine Berechtigung, die Farbe des Clan-Tags zu ändern."
const val INVALID_HEX_COLOR = "Invalid hex color: "

const val NOTHING_CHANGED = "Nothing changed."

/** How many hex digits the foreground and background of a clan tag are written with. */
const val CLAN_TAG_COLOR_LENGTH = 6

/** How many hex digits the shadow of a clan tag is written with; it carries an alpha channel. */
const val CLAN_TAG_SHADOW_COLOR_LENGTH = 8

/**
 * Whether [input] is a hex color of exactly [length] digits, written without a leading number sign.
 */
fun isHexColor(input: String, length: Int) = Regex("^[0-9a-fA-F]{$length}$").matches(input)

/** The hex colors offered for the foreground and background of a clan tag. */
val CLAN_TAG_COLOR_SUGGESTIONS = listOf(
    "#FF6B6B",
    "#4ECDC4",
    "#1A1A2E",
    "#F7B801",
    "#6A4C93",
    "#00C2FF",
    "#2EC4B6",
    "#E71D36"
)

/** The hex colors offered for the shadow of a clan tag, which carries an alpha channel. */
val CLAN_TAG_SHADOW_COLOR_SUGGESTIONS = listOf(
    "#FF6B6BCC",
    "#4ECDC480",
    "#1A1A2ECC",
    "#F7B80199",
    "#6A4C93B3",
    "#00C2FF66",
    "#2EC4B6AA",
    "#E71D3688"
)

/**
 * Tells [targetName]'s clan membership, linking to the clan's information where there is one.
 */
fun whoisMessage(targetName: String, clan: Clan?) = buildText {
    appendInfoPrefix()
    variableValue(targetName)

    if (clan == null) {
        info(" ist in keinem Clan.")
    } else {
        info(" ist im Clan ")
        append {
            variableValue(clan.name)
            hoverEvent(buildText {
                info("Klicke, um Informationen über den Clan anzuzeigen.")
            })
            clickEvent(ClickEvent.runCommand("/clan info ${clan.tag}"))
        }
        info(".")
    }
}

/**
 * Reports that the clan tag color was changed, and that it takes a while to propagate.
 */
fun clanTagColorChangedMessage() = buildText {
    appendSuccessPrefix()
    success("Die Farbe des Clan-Tags wurde erfolgreich geändert.")
    success(" Es kann ein paar Minuten dauern, bis die Änderung Netzwerkweit sichtbar ist.")
}

/**
 * Reports whether the player now accepts clan invitations.
 */
fun clanInvitesToggledMessage(accept: Boolean) = buildText {
    appendSuccessPrefix()
    success("Du hast Einladungen zu Clans ")
    if (accept) {
        variableValue("aktiviert")
    } else {
        variableValue("deaktiviert")
    }
    success(".")
}

/**
 * Reports that the clan configuration was read from disk again.
 */
fun clanConfigReloadedMessage() = buildText {
    appendSuccessPrefix()
    success("Die Clan-Konfiguration wurde neu geladen.")
}

fun invalidatingCachesMessage() = buildText {
    info("Invalidated all caches...")
}

fun invalidatedCachesMessage() = buildText {
    success("Caches invalidated.")
}
