package dev.slne.clan.core

import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.appendText
import dev.slne.surf.surfapi.core.api.messages.buildText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent

@DslMarker
annotation class MessageMarker

@Deprecated(
    message = "Use buildText from surf-api instead",
    replaceWith = ReplaceWith("buildText(builder)", imports = ["dev.slne.surf.surfapi.core.api.messages.buildText"]),
    level = DeprecationLevel.WARNING
)
suspend fun buildMessageAsync(
    prefixed: Boolean = true,
    @MessageMarker builder: suspend TextComponent.Builder.() -> Unit
): Component {
    val textBuilder = Component.text()

    if (prefixed) {
        textBuilder.append(Messages.prefix)
    }

    textBuilder.builder()

    return textBuilder.build()
}

@Deprecated(
    message = "Use buildText from surf-api instead",
    replaceWith = ReplaceWith("buildText(builder)", imports = ["dev.slne.surf.surfapi.core.api.messages.buildText"]),
    level = DeprecationLevel.WARNING
)
fun buildMessage(
    prefixed: Boolean = true,
    @MessageMarker builder: TextComponent.Builder.() -> Unit
): Component {
    val textBuilder = Component.text()

    if (prefixed) {
        textBuilder.append(Messages.prefix)
    }

    textBuilder.builder()

    return textBuilder.build()
}

object Messages {
    val prefix = buildText {
        appendText(">> ", Colors.DARK_SPACER)
        appendText("Clan", Colors.PREFIX_COLOR)
        appendText(" | ", Colors.DARK_SPACER)
    }

    val notInClanComponent = buildMessage {
        appendText("Du bist in keinem Clan.", Colors.ERROR)
    }

    fun unknownClanComponent(clanTag: String) = buildMessage {
        appendText("Der Clan '", Colors.ERROR)
        appendText(clanTag, Colors.VARIABLE_VALUE)
        appendText("' existiert nicht.", Colors.ERROR)
    }

}