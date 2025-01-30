package dev.slne.clan.velocity.listeners

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import dev.slne.clan.core.service.NameCacheService
import org.springframework.stereotype.Component

@Component
class JoinNameCacheListener(
    private val nameCacheService: NameCacheService
) {

    @Subscribe
    suspend fun onLogin(event: LoginEvent) {
        val nameByUuid = nameCacheService.findNameByUuid(event.player.uniqueId)

        if (nameByUuid != null && nameByUuid != event.player.username) {
            nameCacheService.updateNameByUuidCache(event.player.uniqueId, event.player.username)
        } else {
            nameCacheService.createNameCache(event.player.uniqueId, event.player.username)
        }
    }
}