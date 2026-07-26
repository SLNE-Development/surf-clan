package dev.slne.clan.paper.listener

import com.github.shynixn.mccoroutine.folia.asyncDispatcher
import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.clan.paper.plugin
import dev.slne.clan.paper.tagcolor.ClanTagColorEnforcer
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object ClanTagColorJoinListener : Listener {

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val playerUuid = event.player.uniqueId

        plugin.launch(plugin.asyncDispatcher) {
            ClanTagColorEnforcer.enforceForClanOf(playerUuid)
        }
    }
}
