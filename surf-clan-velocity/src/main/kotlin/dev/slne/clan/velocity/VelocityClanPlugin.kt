package dev.slne.clan.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.slne.clan.core.ClanApplication
import dev.slne.clan.core.dataDirectory
import dev.slne.clan.core.getBean
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.commands.ClanCommand
import dev.slne.clan.velocity.listeners.ListenerProcessor
import dev.slne.clan.velocity.placeholder.PlaceholderManager
import java.nio.file.Path

val plugin get() = VelocityClanPlugin.instance

class VelocityClanPlugin @Inject constructor(
    val server: ProxyServer,
    val eventManager: EventManager,
    val container: PluginContainer,
    @DataDirectory val dataPath: Path,
    suspendingPluginContainer: SuspendingPluginContainer
) {

    init {
        instance = this
        suspendingPluginContainer.initialize(this)

        dataDirectory = dataPath

        ClanApplication.run(this.javaClass.classLoader)
    }

    @Subscribe(order = PostOrder.LATE)
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        ClanCommand(getBean<ClanService>(), getBean<ClanPlayerService>()).register()
        ListenerProcessor.registerListeners()
        getBean<PlaceholderManager>().registerPlaceholders()
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        eventManager.unregisterListeners(this)
    }

    companion object {
        lateinit var instance: VelocityClanPlugin
    }

}