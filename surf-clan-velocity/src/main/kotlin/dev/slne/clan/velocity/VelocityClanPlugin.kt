package dev.slne.clan.velocity

import com.github.shynixn.mccoroutine.velocity.SuspendingPluginContainer
import com.google.inject.Inject
import com.velocitypowered.api.event.EventManager
import com.velocitypowered.api.event.PostOrder
import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent
import com.velocitypowered.api.plugin.Dependency
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.PluginContainer
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import dev.slne.clan.core.ClanApplication
import dev.slne.clan.core.dataDirectory
import dev.slne.clan.core.getBean
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.commands.ClanCommand
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.listeners.ListenerProcessor
import me.neznamy.tab.api.TabAPI
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.nio.file.Path

val plugin get() = VelocityClanPlugin.instance

@Plugin(
    id = "surf-clan-velocity",
    name = "Surf Clan",
    dependencies = [
        Dependency(id = "surf-api-velocity", optional = false),
        Dependency(id = "surf-data-velocity", optional = false),
        Dependency(id = "commandapi", optional = false),
        Dependency(id = "tab", optional = false)
    ]
)
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
//        DataApi.run(ClanApplication::class.java, ClanApplication::class.java.classLoader)
        //runApplication(ClanApplication::class.java.classLoader)
    }

    @Subscribe(order = PostOrder.LATE)
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        ClanCommand(getBean<ClanService>(), getBean<ClanPlayerService>()).register()
        ListenerProcessor.registerListeners()

        val lcs = LegacyComponentSerializer.legacySection()
        TabAPI.getInstance().placeholderManager.registerPlayerPlaceholder(
            "%clan_name%",
            100
        ) { player ->
            val velocityPlayer = player.player as Player

            velocityPlayer.findClan(getBean<ClanService>())?.name ?: ""
        }

        TabAPI.getInstance().placeholderManager.registerPlayerPlaceholder(
            "%clan_tag_raw%",
            100
        ) { player ->
            val velocityPlayer = player.player as Player

            velocityPlayer.findClan(getBean<ClanService>())?.tag ?: ""
        }

        TabAPI.getInstance().placeholderManager.registerPlayerPlaceholder(
            "%clan_tag%",
            100
        ) { player ->
            val velocityPlayer = player.player as Player
            val clanTag = velocityPlayer.findClan(getBean<ClanService>())?.tag ?: ""

            if (clanTag.isEmpty()) {
                return@registerPlayerPlaceholder ""
            }

            val clanTagComponent = Component.text()
            clanTagComponent.appendSpace()
            clanTagComponent.append(Component.text("[", NamedTextColor.GRAY))
            clanTagComponent.append(Component.text(clanTag, NamedTextColor.YELLOW))
            clanTagComponent.append(Component.text("]", NamedTextColor.GRAY))

            lcs.serialize(clanTagComponent.build())
        }
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        eventManager.unregisterListeners(this)
    }

    companion object {
        lateinit var instance: VelocityClanPlugin
    }

}