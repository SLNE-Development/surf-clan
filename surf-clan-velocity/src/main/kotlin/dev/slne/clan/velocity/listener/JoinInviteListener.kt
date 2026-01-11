package dev.slne.clan.velocity.listener

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.ServerConnectedEvent
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.components.Components
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.event.ClickEvent
import java.util.concurrent.ConcurrentHashMap
import kotlin.time.Duration.Companion.seconds

object JoinInviteListener {

    @Subscribe
    fun onServerConnected(event: ServerConnectedEvent) {
        plugin.container.launch {
            delay(1.seconds)

            val player = event.player
            if (player.currentServer.isEmpty) {
                return@launch
            }

            val invites = ClanInvite.pendingInvitesByPlayer(player.uniqueId)

            if (invites.isEmpty()) {
                return@launch
            }


            val data = ConcurrentHashMap.newKeySet<ClanInviteRenderData>()

            supervisorScope {
                for (invite in invites) {
                    launch {
                        val clan = invite.getClan() ?: return@launch
                        val renderData = ClanInviteRenderData(
                            clanInformationHover = Components.Clan.renderClanInformationHover(clan as ClanImpl),
                            clanName = clan.name
                        )
                        data.add(renderData)
                    }
                }
            }

            buildText {
                appendPrefix()
                info("Du hast noch ".toSmallCaps())
                variableValue(invites.size)
                info(" offene Clan-Einladung${if (invites.size == 1) "" else "en"}.".toSmallCaps())
                appendCollectionNewLine(data) { it.asComponent() }
            }
        }
    }

    private data class ClanInviteRenderData(
        private val clanInformationHover: Component,
        private val clanName: String
    ) : ComponentLike {
        override fun asComponent(): Component = buildText {
            append(clanInformationHover)
            appendSpace()
            append {
                success("[Annehmen]")
                hoverEvent(buildText {
                    info("Klicke hier, um die Einladung anzunehmen.")
                })
                clickEvent(ClickEvent.runCommand("/clan accept $clanName"))
            }
            appendSpace()
            append {
                error("[Ablehnen]")
                hoverEvent(buildText {
                    info("Klicke hier, um die Einladung abzulehnen.")
                })
                clickEvent(ClickEvent.runCommand("/clan deny $clanName"))
            }
        }
    }
}