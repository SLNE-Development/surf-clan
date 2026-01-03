package dev.slne.clan.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.github.shynixn.mccoroutine.velocity.launch
import com.google.inject.Inject
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.ProxyServer
import dev.slne.clan.core.databaseLoader
import dev.slne.clan.core.service.clanService
import dev.slne.clan.velocity.commands.ClanCommand
import dev.slne.clan.velocity.config.ClanConfig
import dev.slne.clan.velocity.listener.ClanPlayerListener
import dev.slne.clan.velocity.listener.JoinInviteListener
import dev.slne.clan.velocity.listener.JoinResetClanTagColorListener
import dev.slne.clan.velocity.redis.listener.ClanRedisListener
import dev.slne.surf.redis.RedisApi
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

    @Subscribe
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        databaseLoader.connect(dataPath)
        databaseLoader.createTables()

        redisApi = RedisApi.create()
        redisApi.subscribeToEvents(ClanRedisListener)
        clanService.load(redisApi)
        redisApi.freezeAndConnect()

        ClanCommand().register()

        plugin.server.eventManager.register(plugin, ClanPlayerListener)
        plugin.server.eventManager.register(plugin, JoinInviteListener)
        plugin.server.eventManager.register(plugin, JoinResetClanTagColorListener)

        container.launch {
            clanService.refreshClans()
        }
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        eventManager.unregisterListeners(this)

        databaseLoader.disconnect()
        redisApi.disconnect()
    }

    companion object {
        lateinit var instance: VelocityMain
        lateinit var redisApi: RedisApi
    }
}

val clanConfigHolder = ClanConfig.ClanConfigHolder()