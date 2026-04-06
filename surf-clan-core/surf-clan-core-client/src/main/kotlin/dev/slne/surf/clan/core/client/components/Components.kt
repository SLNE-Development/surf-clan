package dev.slne.surf.clan.core.client.components

import dev.slne.clan.api.clan.Clan.Companion.DISCORD_LINK_REQUIRED_MEMBERS
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.CommonComponents
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.clan.core.clan.ClanImpl
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import java.time.format.DateTimeFormatter

object Components {
    private val DATE_TIME_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss")

    object Clan {
        suspend fun renderClanInformationHover(clan: ClanImpl) = buildText {
            val hoverComponent = buildText {
                append(renderClanInformation(clan))
                appendNewline()
                appendNewline()

                text("Klicke, um eine Einladung zum Clan-Discord", Colors.GRAY)
                appendNewline()
                text("zu erhalten.", Colors.GRAY)
            }

            variableValue(clan.name)
            hoverEvent(hoverComponent)
            clickEvent(clan.discordInvite?.let { ClickEvent.openUrl(it) }
                ?: CommonComponents.DISCORD_LINK.clickEvent())
        }

        suspend fun renderClanInformation(clan: ClanImpl) = buildText {
            val createdBy =
                PlayerLookupService.getUsername(clan.createdByUuid) ?: clan.createdByUuid.toString()
            info("Informationen".toSmallCaps(), TextDecoration.BOLD)

            appendNewline {
                appendLine("Name".toSmallCaps(), clan.name)
            }

            appendNewline {
                appendLine("Tag".toSmallCaps(), clan.tag)
            }

            appendNewline {
                appendLine(
                    "Anführer".toSmallCaps(),
                    clan.members.count { it.role == ClanMemberRole.LEADER || it.role == ClanMemberRole.OWNER }
                )
            }

            appendNewline {
                appendLine(
                    "Offiziere".toSmallCaps(),
                    clan.members.count { it.role == ClanMemberRole.OFFICER }
                )
            }

            appendNewline {
                appendLine("Mitglieder".toSmallCaps(), clan.members.size)
            }

            appendNewline {
                appendLine("Erstellt von".toSmallCaps(), createdBy)
            }

            appendNewline {
                appendLine("Erstellt am".toSmallCaps(), DATE_TIME_FORMATTER.format(clan.createdAt))
            }

            if (clan.members.size >= DISCORD_LINK_REQUIRED_MEMBERS) {
                appendNewline {
                    appendLine(
                        "Discord",
                        clan.discordInvite ?: CommonComponents.DISCORD_LINK.color(Colors.WHITE)
                    )
                    clickEvent(clan.discordInvite?.let { ClickEvent.openUrl(it) }
                        ?: CommonComponents.DISCORD_LINK.clickEvent())
                }
            }
        }
    }

    private fun SurfComponentBuilder.appendLine(key: String, value: Any) = append {
        info("| ", TextDecoration.BOLD)
        text("$key: ", Colors.GRAY)
        if (value is ComponentLike) {
            append(value)
        } else {
            text(value.toString(), Colors.WHITE)
        }
    }
}