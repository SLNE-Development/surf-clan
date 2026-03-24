package dev.slne.clan.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.slne.surf.clan.core.ClanInstance
import kotlinx.coroutines.runBlocking
import java.nio.file.Path

val plugin get() = VelocityMain.instance

class VelocityMain @Inject constructor(
    val proxy: ProxyServer,
    val eventManager: EventManager,
    val container: PluginContainer,
    @param:DataDirectory val dataPath: Path,
    suspendingPluginContainer: SuspendingPluginContainer
) {
    init {
        instance = this
        suspendingPluginContainer.initialize(this)

        runBlocking {
            ClanInstance.load()
        }
    }

    @Subscribe
    suspend fun onProxyInitialization(event: ProxyInitializeEvent) {
        ClanInstance.enable()
    }

    @Subscribe
    suspend fun onProxyShutdown(event: ProxyShutdownEvent) {
        ClanInstance.disable()
    }

    companion object {
        lateinit var instance: VelocityMain
    }
}