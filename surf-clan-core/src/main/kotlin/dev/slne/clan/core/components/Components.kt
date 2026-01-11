package dev.slne.clan.core.components

import dev.slne.clan.api.clan.Clan.Companion.DISCORD_LINK_REQUIRED_MEMBERS
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.CommonComponents
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService
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

                text("Klicke, um eine Einladung zum Clan-Discord", Colors.GRAY)
                appendNewline()
                text("zu erhalten.", Colors.GRAY)
            }

            variableValue(clan.name)
            hoverEvent(hoverComponent)
            clickEvent(clan.discordInvite?.let { ClickEvent.openUrl(it) } ?: CommonComponents.DISCORD_LINK.clickEvent())
        }

        suspend fun renderClanInformation(clan: ClanImpl) = buildText {
            val createdBy = PlayerLookupService.getUsername(clan.createdByUuid) ?: clan.createdByUuid.toString()
            info("Informationen".toSmallCaps(), TextDecoration.BOLD)

            appendNewline {
                append(renderLine("Name".toSmallCaps(), clan.name))
            }

            appendNewline {
                append(renderLine("Tag".toSmallCaps(), clan.tag))
            }

            appendNewline {
                append(
                    renderLine(
                        "Anführer".toSmallCaps(),
                        clan.members.count { it.role == ClanMemberRole.LEADER || it.role == ClanMemberRole.OWNER }
                    )
                )
            }

            appendNewline {
                append(
                    renderLine(
                        "Offiziere".toSmallCaps(),
                        clan.members.count { it.role == ClanMemberRole.OFFICER }
                    )
                )
            }

            appendNewline {
                append(renderLine("Mitglieder".toSmallCaps(), clan.members.size))
            }

            appendNewline {
                append(renderLine("Erstellt von".toSmallCaps(), createdBy))
            }

            appendNewline {
                append(renderLine("Erstellt am".toSmallCaps(), DATE_TIME_FORMATTER.format(clan.createdAt)))
            }

            if (clan.members.size >= DISCORD_LINK_REQUIRED_MEMBERS) {
                appendNewline {
                    append(
                        renderLine(
                            "Discord",
                            clan.discordInvite ?: CommonComponents.DISCORD_LINK.color(Colors.WHITE)
                        )
                    )
                    clickEvent(clan.discordInvite?.let { ClickEvent.openUrl(it) } ?: CommonComponents.DISCORD_LINK.clickEvent())
                }
            }
        }
    }

    private fun SurfComponentBuilder.renderLine(key: String, value: Any) = append {
        info("| ", TextDecoration.BOLD)
        text("$key: ", Colors.GRAY)
        if (value is ComponentLike) {
            append(value)
        } else {
            text(value.toString(), Colors.WHITE)
        }
    }
}