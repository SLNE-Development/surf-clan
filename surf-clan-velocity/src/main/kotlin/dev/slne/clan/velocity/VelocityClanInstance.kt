package dev.slne.clan.velocity

import com.google.auto.service.AutoService
import dev.slne.clan.velocity.listener.JoinInviteListener
import dev.slne.surf.clan.core.ClanInstance
import dev.slne.surf.clan.core.client.ClientClanInstance

@AutoService(ClanInstance::class)
class VelocityClanInstance : ClientClanInstance() {
    override val dataPath get() = plugin.dataPath

    override suspend fun enable() {
        super.enable()

        val eventManager = plugin.eventManager
        eventManager.register(plugin, JoinInviteListener)
    }
}