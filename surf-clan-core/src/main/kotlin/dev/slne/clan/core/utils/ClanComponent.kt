package dev.slne.clan.core.utils

import dev.slne.clan.api.Clan
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.buildMessageAsync
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration

val CLAN_COMPONENT_BAR_COLOR = TextColor.fromHexString("#97B3F7")

suspend fun clanComponent(clan: Clan, clanPlayerService: ClanPlayerService) =
    buildMessageAsync(false) {
        val createdBy =
            clanPlayerService.findClanPlayerByUuid(clan.createdBy)?.username ?: "Unbekannt"

        val hoverComponent = buildMessage(false) {
            append(Component.text("ɪɴғᴏʀᴍᴀᴛɪᴏɴᴇɴ", CLAN_COMPONENT_BAR_COLOR, TextDecoration.BOLD))
            appendNewline()

            append(renderLine("ɴᴀᴍᴇ", clan.name))
            appendNewline()

            append(renderLine("ᴛᴀɢ", clan.tag))
            appendNewline()

            append(renderLine("ᴇʀsᴛᴇʟʟᴛ ᴠᴏɴ", createdBy))
            appendNewline()

            append(renderLine("ᴍɪᴛɢʟɪᴇᴅᴇʀ", clan.members.size.toString()))
            appendNewline()

            append(renderLine("ᴇɪɴʟᴀᴅᴜɴɢᴇɴ", clan.invites.size.toString()))
            appendNewline()

            append(
                renderLine(
                    "ᴀɴғüʜʀᴇʀ",
                    clan.members.filter { it.role == ClanMemberRole.LEADER }.size.toString()
                )
            )
            appendNewline()

            append(
                renderLine(
                    "ᴏғғɪᴢɪᴇʀᴇ",
                    clan.members.filter { it.role == ClanMemberRole.OFFICER }.size.toString()
                )
            )
            appendNewline()

            append(renderLine("ᴇʀsᴛᴇʟʟᴛ ᴀᴍ", clan.createdAt?.formatted() ?: "/"))
            appendNewline()

            append(renderLine("ᴀᴋᴛᴜᴀʟɪsɪᴇʀᴛ ᴀᴍ", clan.updatedAt?.formatted() ?: "/"))

            appendNewline()
            appendNewline()

            append(
                Component.text(
                    "Klicke, um eine Einladung zum Clan-Discord",
                    NamedTextColor.GRAY
                )
            )
            appendNewline()
            append(Component.text(" zu erhalten.", NamedTextColor.GRAY))
        }

//        append(Component.text("Beschreibung: ", COLOR_INFO))
//        val description = clan.description ?: "Keine Beschreibung"
//        val split = splitDescription(description)
//
//        split.forEach {
//            append(Component.text(it, COLOR_VARIABLE))
//            if (split.isNotEmpty()) appendNewline()
//        }
//        appendNewline()

//        append(Component.text("Discord Einladung: ", COLOR_INFO))
//        append(Component.text(clan.discordInvite ?: "Keine Einladung", COLOR_VARIABLE))
//        appendNewline()

        append(Component.text(clan.name, Colors.VARIABLE_VALUE))
        hoverEvent(HoverEvent.showText(hoverComponent))
        clickEvent(ClickEvent.openUrl(clan.discordInvite ?: "https://discord.gg/castcrafter"))
    }

private fun renderLine(key: String, value: String) =
    Component.text()
        .append(Component.text("| ", CLAN_COMPONENT_BAR_COLOR, TextDecoration.BOLD))
        .append(Component.text("$key: ", NamedTextColor.GRAY))
        .append(Component.text(value, NamedTextColor.WHITE))

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