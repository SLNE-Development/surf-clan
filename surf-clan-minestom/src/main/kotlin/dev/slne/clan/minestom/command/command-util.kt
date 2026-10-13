package dev.slne.clan.minestom.command

import dev.slne.surf.api.minestom.command.CommandAPI
import dev.slne.surf.api.core.messages.adventure.buildText

fun failCommand(message: String): Nothing =
    CommandAPI.failWithMessage(buildText {
        appendErrorPrefix()
        error(message)
    })