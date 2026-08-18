package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.surf.api.core.messages.adventure.buildText

private val DISCORD_LINK_REGEX =
    """^(?:https?://)?(?:www\.)?(?:discord\.gg|discord(?:app)?\.com/invite)/[A-Za-z0-9-]+/?$""".toRegex()

/** The literal a player passes to remove their clan's Discord link. */
const val REMOVE_DISCORD_LINK = "NULL"

val DISCORD_LINK_SUGGESTIONS = listOf(
    "https://discord.gg/castcrafter",
    "https://discord.com/invite/castcrafter",
    REMOVE_DISCORD_LINK
)

const val NO_DISCORD_PERMISSION = "Du hast keine Berechtigung, den Discord Link zu ändern."
const val INVALID_DISCORD_LINK = "Du musst einen gültigen Discord-Invite Link angeben!"

const val NOT_ENOUGH_MEMBERS_FOR_DISCORD_LINK =
    "Dein Clan muss mindestens ${Clan.DISCORD_LINK_REQUIRED_MEMBERS} aktive Mitglieder haben, um den Discord-Link ändern zu können."

fun isValidDiscordInvite(link: String) = link.matches(DISCORD_LINK_REGEX)

/**
 * Reports that the clan's Discord link was set to [rawLink], or removed when [removed] is set.
 */
fun discordLinkChangedMessage(removed: Boolean, rawLink: String) = buildText {
    appendSuccessPrefix()
    success("Der Discord Link wurde erfolgreich ")
    if (removed) {
        success("entfernt.")
    } else {
        success("auf ")
        variableValue(rawLink)
        success(" gesetzt.")
    }
}
