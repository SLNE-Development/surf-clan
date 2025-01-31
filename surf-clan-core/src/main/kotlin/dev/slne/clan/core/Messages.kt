package dev.slne.clan.core

import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.NamedTextColor

@DslMarker
annotation class MessageMarker

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

    val prefix = Component.text().append(Component.text(">> ", NamedTextColor.DARK_GRAY))
        .append(Component.text("Clan", NamedTextColor.AQUA))
        .append(Component.text(" | ", NamedTextColor.DARK_GRAY)).build()

    val notInClanComponent = buildMessage {
        append(Component.text("Du bist in keinem Clan.", Colors.ERROR))
    }

}