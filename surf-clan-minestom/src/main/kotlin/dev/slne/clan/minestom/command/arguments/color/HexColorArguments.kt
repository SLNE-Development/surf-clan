package dev.slne.clan.minestom.command.arguments.color

import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.argument.Argument
import dev.slne.minestom.lobby.api.command.commandapi.argument.CustomArgument
import dev.slne.minestom.lobby.api.command.commandapi.argument.TextArgument
import dev.slne.minestom.lobby.api.command.commandapi.suggestion.ArgumentSuggestions
import dev.slne.surf.clan.core.client.command.*
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor

/**
 * Reads a hex color of exactly [requiredLength] digits, written without a leading number sign.
 */
abstract class BaseHexColorArgument<T>(
    nodeName: String,
    requiredLength: Int,
    suggestions: List<String>,
    factory: (String) -> T?
) : CustomArgument<T, String>(TextArgument(nodeName), { info ->
    val input = info.currentInput.trim()

    if (!isHexColor(input, requiredLength)) {
        CommandAPI.failWithString("$INVALID_HEX_COLOR$input")
    }

    factory(input) ?: CommandAPI.failWithString("$INVALID_HEX_COLOR$input")
}) {
    init {
        replaceSuggestions(ArgumentSuggestions.strings(suggestions))
    }
}

class ClanTagHexColorArgument(nodeName: String) : BaseHexColorArgument<TextColor>(
    nodeName,
    CLAN_TAG_COLOR_LENGTH,
    CLAN_TAG_COLOR_SUGGESTIONS,
    { TextColor.fromHexString("#$it") }
)

class ClanTagShadowHexColorArgument(nodeName: String) : BaseHexColorArgument<ShadowColor>(
    nodeName,
    CLAN_TAG_SHADOW_COLOR_LENGTH,
    CLAN_TAG_SHADOW_COLOR_SUGGESTIONS,
    { ShadowColor.fromHexString("#$it") }
)

inline fun CommandAPICommand.clanTagHexColorArgument(
    nodeName: String,
    block: Argument<TextColor>.() -> Unit = {}
): CommandAPICommand = withArguments(ClanTagHexColorArgument(nodeName).apply(block))

inline fun CommandAPICommand.clanTagShadowHexColorArgument(
    nodeName: String,
    block: Argument<ShadowColor>.() -> Unit = {}
): CommandAPICommand = withArguments(ClanTagShadowHexColorArgument(nodeName).apply(block))
