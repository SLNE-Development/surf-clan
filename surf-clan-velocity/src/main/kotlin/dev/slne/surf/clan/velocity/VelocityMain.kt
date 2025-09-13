package dev.slne.surf.clan.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.proxy.ProxyServer
import dev.slne.surf.clan.ClanApplication
import dev.slne.surf.clan.core.common.InternalContextHolderImpl
import dev.slne.surf.cloud.api.common.CloudInstance
import dev.slne.surf.cloud.api.common.startSpringApplication

class VelocityMain @Inject constructor(
    val proxy: ProxyServer,
    val container: PluginContainer,
    val suspendingContainer: SuspendingPluginContainer,
) {

    init {
        instance = this

        InternalContextHolderImpl.INSTANCE.context =
            CloudInstance.startSpringApplication(ClanApplication::class)
    }

    @Subscribe
    fun onProxyInitialize(event: ProxyInitializeEvent) {
        suspendingContainer.initialize(this)
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {

    }

    companion object {
        lateinit var instance: VelocityMain
    }
}

val plugin get() = VelocityMain.instance
val proxy get() = plugin.proxy
val container get() = plugin.container