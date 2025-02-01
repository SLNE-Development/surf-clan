package dev.slne.clan.velocity.listeners

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerConnectedEvent
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.buildMessageAsync
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClanInvites
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import kotlinx.coroutines.delay
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

@org.springframework.stereotype.Component
class JoinInviteListener(
    private val clanService: ClanService,
    private val clanPlayerService: ClanPlayerService
) {

    @Subscribe
    suspend fun onLogin(event: ServerConnectedEvent) {

        plugin.container.launch {
            delay(1000L)

            val player = event.player
            val invites = player.findClanInvites<CoreClanInvite>(clanService)

            if (invites.isEmpty()) {
                return@launch
            }

            player.sendMessage(buildMessage(false) {
                append(Component.text("ᴅᴜ ʜᴀsᴛ ɴᴏᴄʜ ", Colors.INFO))
                append(Component.text("${invites.size}", Colors.VARIABLE_VALUE))
                append(Component.text(" ᴏғғᴇɴᴇ ᴄʟᴀɴ-ᴇɪɴʟᴀᴅᴜɴɢ${if (invites.size==1)"" else "ᴇɴ"}:", Colors.INFO))
            })

            for (invite in invites) {
                val clan = invite.clan

                val acceptComponent = buildMessage(false) {
                    append(Component.text(" [Annehmen]", Colors.SUCCESS))
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

                player.sendMessage(buildMessageAsync(false) {
                    append(Component.text(" - ", Colors.INFO))
                    append(clanComponent(clan, clanPlayerService))
                    append(acceptComponent)
                    appendSpace()
                    append(denyComponent)
                })
            }
        }
    }}