package dev.slne.clan.paper.commands.arguments.color

import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.slne.clan.paper.commands.failCommand
import dev.slne.surf.clan.core.client.command.*

abstract class BaseHexColorArgument<T>(
    nodeName: String,
    private val requiredLength: Int,
    private val factory: (String) -> T?
) : CustomArgument<T, String>(TextArgument(nodeName), { info ->
    val input = info.currentInput().trim()

    if (!isHexColor(input, requiredLength)) {
        failCommand("$INVALID_HEX_COLOR ${info.currentInput}")
    }

    factory(input)
        ?: failCommand("$INVALID_HEX_COLOR ${info.currentInput}")
}) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { _ ->
            if (requiredLength == CLAN_TAG_COLOR_LENGTH) {
                CLAN_TAG_COLOR_SUGGESTIONS
            } else {
                CLAN_TAG_SHADOW_COLOR_SUGGESTIONS
            }
        })
    }
}
