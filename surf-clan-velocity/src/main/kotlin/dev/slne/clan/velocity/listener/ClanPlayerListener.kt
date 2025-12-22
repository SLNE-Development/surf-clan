package dev.slne.clan.velocity.listener

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.clan.core.service.clanPlayerService

object ClanPlayerListener {
    @Subscribe
    suspend fun onLogin(event: LoginEvent) {
        val clanPlayer = clanPlayerService.findClanPlayerByUuid(event.player.uniqueId)

        if (clanPlayer != null) {
            if (clanPlayer.username == event.player.username) return

            clanPlayer.username = event.player.username
            clanPlayerService.save(clanPlayer)
        } else {
            clanPlayerService.save(
                ClanPlayer(
                    uuid = event.player.uniqueId,
                    username = event.player.username
                )
            )
        }
    }
}