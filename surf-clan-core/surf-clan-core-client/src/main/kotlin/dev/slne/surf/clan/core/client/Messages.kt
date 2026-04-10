package dev.slne.surf.clan.core.client

import dev.slne.surf.api.core.messages.adventure.buildText

@DslMarker
annotation class MessageMarker

object Messages {
    val notInClanComponent = buildText {
        appendErrorPrefix()
        error("Du bist in keinem Clan.")
    }

    fun unknownClanComponent(clanTag: String) = buildText {
        appendErrorPrefix()
        error("Der Clan ")
        variableValue(clanTag)
        error(" existiert nicht.")
    }

}