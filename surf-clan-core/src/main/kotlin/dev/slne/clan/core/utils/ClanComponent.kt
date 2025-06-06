package dev.slne.clan.core.utils

import dev.slne.clan.api.Clan
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.utils.ClanSettings.DISCORD_LINK_REQUIRED_MEMBERS
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

suspend fun clanComponent(clan: Clan, clanPlayerService: ClanPlayerService) =
    buildText {
        val createdBy =
            clanPlayerService.findClanPlayerByUuid(clan.createdBy)?.username ?: "Unbekannt"

        val hoverComponent = buildText {
            append(Component.text("ɪɴғᴏʀᴍᴀᴛɪᴏɴᴇɴ", Colors.INFO, TextDecoration.BOLD))
            appendNewline()

            append(renderLine("ɴᴀᴍᴇ", clan.name))
            appendNewline()

            append(renderLine("ᴛᴀɢ", clan.tag))
            appendNewline()

            append(
                renderLine(
                    "ᴀɴғüʜʀᴇʀ",
                    clan.members.count { it.role == ClanMemberRole.LEADER || it.role == ClanMemberRole.OWNER }
                )
            )
            appendNewline()

            append(
                renderLine(
                    "ᴏғғɪᴢɪᴇʀᴇ",
                    clan.members.count { it.role == ClanMemberRole.OFFICER }
                )
            )
            appendNewline()

            append(renderLine("ᴍɪᴛɢʟɪᴇᴅᴇʀ", clan.members.size))
            appendNewline()

            append(renderLine("ᴇʀsᴛᴇʟʟᴛ ᴠᴏɴ", createdBy))
            appendNewline()

            append(renderLine("ᴇʀsᴛᴇʟʟᴛ ᴀᴍ", clan.createdAt?.formatted() ?: "/"))
            appendNewline()

            if (clan.members.size >= DISCORD_LINK_REQUIRED_MEMBERS) {
                append(
                    renderLine(
                        "ᴅɪsᴄᴏʀᴅ",
                        clan.discordInvite ?: "https://discord.gg/castcrafter"
                    )
                )
                appendNewline()
            }

            appendNewline()

            append(
                Component.text(
                    "Klicke, um eine Einladung zum Clan-Discord",
                    NamedTextColor.GRAY
                )
            )
            appendNewline()
            append(Component.text("zu erhalten.", NamedTextColor.GRAY))
        }

        append(Component.text(clan.name, Colors.VARIABLE_VALUE))
        hoverEvent(HoverEvent.showText(hoverComponent))
        clickEvent(ClickEvent.openUrl(clan.discordInvite ?: "https://discord.gg/castcrafter"))
    }

private fun renderLine(key: String, value: Any) =
    Component.text()
        .append(Component.text("| ", Colors.INFO, TextDecoration.BOLD))
        .append(Component.text("$key: ", NamedTextColor.GRAY))
        .append(Component.text(value.toString(), NamedTextColor.WHITE))