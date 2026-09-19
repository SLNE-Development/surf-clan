package dev.slne.clan.paper.commands

import dev.jorel.commandapi.CommandAPIPaper
import dev.slne.surf.api.core.messages.adventure.buildText

fun failCommand(message: String): Nothing =
    throw CommandAPIPaper.failWithAdventureComponent(buildText {
        appendErrorPrefix()
        error(message)
    })