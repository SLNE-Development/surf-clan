package dev.slne.surf.clan.core.client

import dev.slne.surf.api.core.messages.adventure.buildText

@DslMarker
annotation class MessageMarker

/**
 * The player facing texts shared by every platform.
 *
 * Constants hold the plain texts a command reports through its platform's own failure mechanism;
 * the components hold the ones that are sent directly.
 */
object Messages {
    const val NOT_IN_CLAN = "Du bist in keinem Clan."
    const val NO_LONGER_IN_A_CLAN = "Du bist nicht mehr in einem Clan."
    const val ALREADY_IN_CLAN = "Du bist bereits in einem Clan."
    const val PLAYER_NOT_FOUND = "Der Spieler wurde nicht gefunden."
    const val PLAYER_NOT_IN_YOUR_CLAN = "Der Spieler ist nicht in deinem Clan."

    val notInClanComponent = buildText {
        appendErrorPrefix()
        error(NOT_IN_CLAN)
    }

    fun unknownClanComponent(clanTag: String) = buildText {
        appendErrorPrefix()
        error("Der Clan ")
        variableValue(clanTag)
        error(" existiert nicht.")
    }

    fun unknownClanWithTag(tag: String) = "Unknown clan with tag '$tag'"

    fun unknownClanMember(nameOrUuid: String) = "Clan Member '$nameOrUuid' not found."

    fun noPendingInviteForClan(clanName: String) = "No pending invite for clan '$clanName'."

    fun unknownPlayer(name: String) = "Player '$name' not found."
}
