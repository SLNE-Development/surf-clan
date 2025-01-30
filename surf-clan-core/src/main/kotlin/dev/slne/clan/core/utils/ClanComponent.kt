package dev.slne.clan.core.utils

import dev.slne.clan.api.Clan
import dev.slne.clan.core.COLOR_INFO
import dev.slne.clan.core.COLOR_VARIABLE
import dev.slne.clan.core.buildMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent

fun clanComponent(clan: Clan) = buildMessage(false) {
    val hoverComponent = buildMessage(false) {
        append(Component.text("Name: ", COLOR_INFO))
        append(Component.text(clan.name, COLOR_VARIABLE))
        appendNewline()

        append(Component.text("Tag: ", COLOR_INFO))
        append(Component.text(clan.tag, COLOR_VARIABLE))
        appendNewline()

//        append(Component.text("Beschreibung: ", COLOR_INFO))
//        val description = clan.description ?: "Keine Beschreibung"
//        val split = splitDescription(description)
//
//        split.forEach {
//            append(Component.text(it, COLOR_VARIABLE))
//            if (split.isNotEmpty()) appendNewline()
//        }
//        appendNewline()

        append(Component.text("Erstellt von: ", COLOR_INFO))
        append(Component.text(clan.createdBy.toString(), COLOR_VARIABLE)) // TODO: Change to name
        appendNewline()

//        append(Component.text("Discord Einladung: ", COLOR_INFO))
//        append(Component.text(clan.discordInvite ?: "Keine Einladung", COLOR_VARIABLE))
//        appendNewline()

        append(Component.text("Mitglieder: ", COLOR_INFO))
        append(Component.text(clan.members.size.toString(), COLOR_VARIABLE))
        appendNewline()

        append(Component.text("Einladungen: ", COLOR_INFO))
        append(Component.text(clan.invites.size.toString(), COLOR_VARIABLE))
        appendNewline()

        append(Component.text("Erstellt am: ", COLOR_INFO))
        append(Component.text(clan.createdAt?.formatted() ?: "/", COLOR_VARIABLE))
        appendNewline()

        append(Component.text("Aktualisiert am: ", COLOR_INFO))
        append(Component.text(clan.updatedAt?.formatted() ?: "/", COLOR_VARIABLE))
    }

    append(Component.text(clan.name, COLOR_VARIABLE))
    hoverEvent(HoverEvent.showText(hoverComponent))
    clickEvent(ClickEvent.openUrl(clan.discordInvite ?: "https://discord.gg/castcrafter"))
}

private fun splitDescription(description: String, maxLength: Int = 50): List<String> {
    val lines = mutableListOf<String>()

    var currentLine = ""
    var currentLength = 0

    for (word in description.split(" ")) {
        if (currentLength + word.length > maxLength) {
            lines.add(currentLine)
            currentLine = ""
            currentLength = 0
        }

        currentLine += "$word "
        currentLength += word.length
    }

    lines.add(currentLine)

    return lines
}