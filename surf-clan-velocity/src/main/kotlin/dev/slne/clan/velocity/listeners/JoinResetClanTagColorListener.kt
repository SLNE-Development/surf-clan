package dev.slne.clan.velocity.listeners

import com.github.shynixn.mccoroutine.velocity.launch
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.player.PlayerChooseInitialServerEvent
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import dev.slne.surf.bitmap.bitmaps.Bitmaps
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class JoinResetClanTagColorListener(
    private val clanService: ClanService
) {

    @Subscribe
    fun onJoin(event: PlayerChooseInitialServerEvent) {
        val player = event.player
        val clan = player.findClan(clanService) ?: return

        plugin.container.launch {
            if (player.hasPermission("surf.clan.options.tagcolor")) {
                return@launch
            }

            if (clan.createdBy != player.uniqueId) {
                return@launch
            }

            val defaultTagColor = Bitmaps.CLAN_DEFAULT

            if (clan.clanTagColor != defaultTagColor) {
                clan.clanTagColor = defaultTagColor

                player.sendText {
                    appendPrefix()

                    info("Du besitzt keine Berechtigung, die Farbe des Clan-Tags zu ändern.")
                    info("Die Farbe wurde auf die Standardfarbe zurückgesetzt und wird in wenigen Sekunden Netzwerkweit aktualisiert.")
                }
            }

        }
    }
}