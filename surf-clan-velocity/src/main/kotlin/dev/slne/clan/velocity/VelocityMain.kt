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
import dev.slne.clan.core.databaseLoader
import dev.slne.clan.velocity.commands.ClanCommand
import dev.slne.clan.velocity.config.ClanConfig
import dev.slne.clan.velocity.listener.ClanPlayerListener
import dev.slne.clan.velocity.listener.JoinInviteListener
import dev.slne.clan.velocity.listener.JoinResetClanTagColorListener
import dev.slne.clan.velocity.placeholder.placeholderManager
import java.nio.file.Path

val plugin get() = VelocityMain.instance

class VelocityMain @Inject constructor(
    val server: ProxyServer,
    val eventManager: EventManager,
    val container: PluginContainer,
    @param:DataDirectory val dataPath: Path,
    suspendingPluginContainer: SuspendingPluginContainer
) {

    init {
        instance = this
        suspendingPluginContainer.initialize(this)
    }

    @Subscribe(order = PostOrder.LATE)
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        databaseLoader.connect(dataPath)
        databaseLoader.createTables()

        ClanCommand().register()
        placeholderManager.registerPlaceholders()

        plugin.server.eventManager.register(plugin, ClanPlayerListener)
        plugin.server.eventManager.register(plugin, JoinInviteListener)
        plugin.server.eventManager.register(plugin, JoinResetClanTagColorListener)
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        eventManager.unregisterListeners(this)

        databaseLoader.disconnect()
    }

    companion object {
        lateinit var instance: VelocityMain
    }
}

val clanConfigHolder = ClanConfig.ClanConfigHolder()