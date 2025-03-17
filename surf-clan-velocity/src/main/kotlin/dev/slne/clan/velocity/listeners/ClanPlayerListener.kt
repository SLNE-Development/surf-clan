package dev.slne.clan.velocity.listeners

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import dev.slne.clan.core.player.CoreClanPlayer
import org.springframework.stereotype.Component

@Component
class ClanPlayerListener(
    private val clanPlayerService: ClanPlayerService
) {

    @Subscribe
    suspend fun onLogin(event: LoginEvent) {
        val clanPlayer = clanPlayerService.findClanPlayerByUuid(event.player.uniqueId)

        if (clanPlayer != null) {
            if (clanPlayer.username == event.player.username) return

            clanPlayer.username = event.player.username
            clanPlayerService.save(clanPlayer)
        } else {
            clanPlayerService.save(
                CoreClanPlayer(
                    uuid = event.player.uniqueId,
                    username = event.player.username
                )
            )
        }
    }
}