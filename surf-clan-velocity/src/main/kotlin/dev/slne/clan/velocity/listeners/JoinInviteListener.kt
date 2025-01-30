package dev.slne.clan.velocity.listeners

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.LoginEvent
import dev.slne.clan.core.service.ClanService
import org.springframework.stereotype.Component

@Component
class JoinInviteListener(
    private val clanService: ClanService
) {

    @Subscribe
    suspend fun onLogin(event: LoginEvent) {
        
    }
}