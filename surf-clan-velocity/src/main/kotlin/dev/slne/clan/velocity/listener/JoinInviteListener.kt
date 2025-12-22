package dev.slne.clan.velocity.listener

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerConnectedEvent
import dev.slne.clan.core.service.clanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClanInvites
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

object JoinInviteListener {
    @Subscribe
    fun onLogin(event: ServerConnectedEvent) {
        plugin.container.launch {
            delay(1000L)

            val player = event.player
            val invites = player.findClanInvites()

            if (invites.isEmpty()) {
                return@launch
            }

            player.sendMessage(buildText {
                append(Component.text("ᴅᴜ ʜᴀsᴛ ɴᴏᴄʜ ", Colors.INFO))
                append(Component.text("${invites.size}", Colors.VARIABLE_VALUE))
                append(
                    Component.text(
                        " ᴏғғᴇɴᴇ ᴄʟᴀɴ-ᴇɪɴʟᴀᴅᴜɴɢ${if (invites.size == 1) "" else "ᴇɴ"}:",
                        Colors.INFO
                    )
                )
            })

            for (invite in invites) {
                val clan = clanService.findClanByInvite(invite) ?: return@launch
                val acceptComponent = buildText {
                    append(Component.text(" [Annehmen]", Colors.SUCCESS))
                    hoverEvent(
                        HoverEvent.showText(
                            Component.text(
                                "Klicke hier, um die Einladung anzunehmen.",
                                NamedTextColor.GREEN
                            )
                        )
                    )
                    clickEvent(ClickEvent.runCommand("/clan accept ${clan.name}"))
                }

                val denyComponent = buildText {
                    append(Component.text("[Ablehnen]", Colors.ERROR))
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

                player.sendMessage(buildText {
                    append(Component.text(" - ", Colors.INFO))
                    append(clanComponent(clan))
                    append(acceptComponent)
                    appendSpace()
                    append(denyComponent)
                })
            }
        }
    }
}