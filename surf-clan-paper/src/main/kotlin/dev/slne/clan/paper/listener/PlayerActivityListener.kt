package dev.slne.clan.paper.listener

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.clan.CoreClanService
import dev.slne.clan.paper.plugin
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object PlayerActivityListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        plugin.launch {
            val clan =
                CoreClanService.findClanByPlayer(event.player.uniqueId) as? ClanImpl
                    ?: return@launch
            CoreClanService.updateLastActivity(clan)
        }
    }
}
