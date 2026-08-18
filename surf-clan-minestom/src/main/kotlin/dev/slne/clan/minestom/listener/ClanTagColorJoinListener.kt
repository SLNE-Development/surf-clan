package dev.slne.clan.minestom.listener

import com.google.inject.Inject
import dev.slne.minestom.lobby.api.coroutine.minestomAsyncScope
import dev.slne.minestom.lobby.api.event.EventRegistrar
import dev.slne.minestom.lobby.api.extension.addListener
import dev.slne.surf.clan.core.client.tagcolor.ClanTagColorEnforcer
import kotlinx.coroutines.launch
import net.minestom.server.event.Event
import net.minestom.server.event.EventNode
import net.minestom.server.event.player.PlayerSpawnEvent

/**
 * Checks the clan tag color of a joining player's clan against its owner's permissions.
 */
class ClanTagColorJoinListener @Inject constructor() : EventRegistrar {
    override fun register(node: EventNode<Event>) {
        node.addListener<PlayerSpawnEvent> { event ->
            if (!event.isFirstSpawn) return@addListener

            val playerUuid = event.player.uuid

            minestomAsyncScope.launch {
                ClanTagColorEnforcer.enforceForClanOf(playerUuid)
            }
        }
    }
}
