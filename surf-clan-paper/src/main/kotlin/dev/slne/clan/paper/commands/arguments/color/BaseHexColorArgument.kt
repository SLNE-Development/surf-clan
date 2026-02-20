package dev.slne.clan.paper.commands.arguments.color

import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.CustomArgument
import dev.jorel.commandapi.arguments.TextArgument

abstract class BaseHexColorArgument<T>(
    nodeName: String,
    private val requiredLength: Int,
    private val factory: (String) -> T?
) : CustomArgument<T, String>(TextArgument(nodeName), { info ->

    val input = info.currentInput().trim()

    val regex = Regex("^[0-9a-fA-F]{$requiredLength}$")

    if (!regex.matches(input)) {
        throw CustomArgumentException.fromMessageBuilder(
            MessageBuilder()
                .append("Invalid hex color: ")
                .appendArgInput()
        )
    }

    factory(input)
        ?: throw CustomArgumentException.fromMessageBuilder(
            MessageBuilder()
                .append("Invalid hex color: ")
                .appendArgInput()
        )
}) {
    init {
        replaceSuggestions(ArgumentSuggestions.stringCollection { _ ->
            if (requiredLength == 6) {
                listOf(
                    "#FF6B6B",
                    "#4ECDC4",
                    "#1A1A2E",
                    "#F7B801",
                    "#6A4C93",
                    "#00C2FF",
                    "#2EC4B6",
                    "#E71D36"
                )
            } else {
                listOf(
                    "#FF6B6BCC",
                    "#4ECDC480",
                    "#1A1A2ECC",
                    "#F7B80199",
                    "#6A4C93B3",
                    "#00C2FF66",
                    "#2EC4B6AA",
                    "#E71D3688"
                )
            }
        })
    }
}