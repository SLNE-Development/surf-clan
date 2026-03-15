package dev.slne.clan.velocity

import com.google.auto.service.AutoService
import dev.slne.clan.core.ClanInstance
import dev.slne.clan.velocity.listener.JoinInviteListener

@AutoService(ClanInstance::class)
class VelocityClanInstance : ClanInstance {
    override val dataPath get() = plugin.dataPath

    override suspend fun enable() {
        super.enable()

        val eventManager = plugin.eventManager
        eventManager.register(plugin, JoinInviteListener)
    }
}