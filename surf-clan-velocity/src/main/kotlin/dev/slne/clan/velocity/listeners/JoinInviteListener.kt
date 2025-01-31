package dev.slne.clan.velocity.listeners

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import dev.slne.clan.core.*
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.CLAN_COMPONENT_BAR_COLOR
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClanInvites
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

@org.springframework.stereotype.Component
class JoinInviteListener(
    private val clanService: ClanService,
    private val clanPlayerService: ClanPlayerService
) {

    @Subscribe
    suspend fun onLogin(event: LoginEvent) {
        val player = event.player
        val invites = player.findClanInvites<CoreClanInvite>(clanService)

        if (invites.isEmpty()) {
            return
        }

        player.sendMessage(buildMessage {
            append(Component.text("ᴅᴜ ʜᴀsᴛ ɴᴏᴄʜ ", COLOR_INFO))
            append(Component.text("${invites.size} ", COLOR_VARIABLE))
            append(Component.text(" ᴏғғᴇɴᴇ ᴄʟᴀɴ-ᴇɪɴʟᴀᴅᴜɴɢᴇɴ.", COLOR_INFO))
        })

        for (invite in invites) {
            val clan = invite.clan

            val acceptComponent = buildMessage(false) {
                append(Component.text("[Annehmen]", COLOR_SUCCESS))
                hoverEvent(
                    HoverEvent.showText(
                        Component.text(
                            "Klicke hier, um die Einladung anzunehmen.",
                            NamedTextColor.GREEN
                        )
                    )
                )
                clickEvent(ClickEvent.runCommand("/clan invite ${player.username} accept ${clan.name}"))
            }

            val denyComponent = buildMessage(false) {
                append(Component.text("[Ablehnen]", COLOR_ERROR))
                hoverEvent(
                    HoverEvent.showText(
                        Component.text(
                            "Klicke hier, um die Einladung abzulehnen.",
                            NamedTextColor.RED
                        )
                    )
                )
                clickEvent(ClickEvent.runCommand("/clan invite ${player.username} deny ${clan.name}"))
            }

            player.sendMessage(buildMessageAsync(false) {
                append(Component.text("| ", CLAN_COMPONENT_BAR_COLOR, TextDecoration.BOLD))
                append(clanComponent(clan, clanPlayerService))
                append(acceptComponent)
                appendSpace()
                append(denyComponent)
            })
        }
    }
}