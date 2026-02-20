package dev.slne.clan.paper.commands.arguments.color

import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.TextArgument

abstract class BaseHexColorArgument<T>(
    nodeName: String,
    private val factory: (String) -> T?
) : CustomArgument<T, String>(TextArgument(nodeName), { info ->

    val normalized = HexColorParser.normalizeToRgba(info.currentInput())
        ?: throw CustomArgumentException.fromMessageBuilder(
            MessageBuilder()
                .append("Failed to parse hex color code: ")
                .appendArgInput()
        )

    factory(normalized)
        ?: throw CustomArgumentException.fromMessageBuilder(
            MessageBuilder()
                .append("Invalid hex color: ")
                .appendArgInput()
        )
}) {
    init {
        replaceSuggestions(HexColorSuggestions.create())
    }
}