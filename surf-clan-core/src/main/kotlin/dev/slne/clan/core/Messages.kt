package dev.slne.clan.core

import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object Messages {
    val notInClanComponent = buildText {
        appendPrefix()

        error("Du bist in keinem Clan.")
    }

    fun unknownClanComponent(clanTag: String) = buildText {
        appendPrefix()

        error("Der Clan ")
        variableValue(clanTag)
        error(" existiert nicht.")
    }

}