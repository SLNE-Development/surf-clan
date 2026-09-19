package dev.slne.clan.paper.commands

import dev.jorel.commandapi.CommandAPI
import dev.slne.surf.api.core.messages.ComponentMessage
import dev.slne.surf.api.core.messages.adventure.buildText

fun failCommand(message: String): Nothing =
    throw CommandAPI.failWithMessage(ComponentMessage(buildText {
        appendErrorPrefix()
        error(message)
    }))