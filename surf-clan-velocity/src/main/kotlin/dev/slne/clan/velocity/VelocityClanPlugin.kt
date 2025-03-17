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
import dev.slne.clan.velocity.commands.ClanCommand
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.listeners.ListenerProcessor
import dev.slne.surf.database.DatabaseProvider
import me.neznamy.tab.api.TabAPI
import me.neznamy.tab.api.TabPlayer
import me.neznamy.tab.api.event.plugin.TabLoadEvent
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer
import java.nio.file.Path
import kotlin.io.path.div


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

        DatabaseProvider(dataPath, dataPath / "storage").connect()
    }

    @Subscribe(order = PostOrder.LATE)
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        ClanCommand.register()
        ListenerProcessor.registerListeners()
        registerPlaceholder()

        TabAPI.getInstance().eventBus!!.register(TabLoadEvent::class.java) { registerPlaceholder() }
    }

    private fun registerPlaceholder() {
        with(TabAPI.getInstance().placeholderManager) {
            registerPlayerPlaceholder(
                "%clan_name%",
                10000
            ) { player ->
                val velocityPlayer = player.player as Player

                velocityPlayer.findClan()?.name ?: ""
            }

            registerPlayerPlaceholder(
                "%clan_tag_raw%",
                10000
            ) { player ->
                val velocityPlayer = player.player as Player

                velocityPlayer.findClan()?.tag ?: ""
            }

            registerPlayerPlaceholder(
                "%clan_tag%",
                10000
            ) { player -> renderClanTag(player) }

            repeat(101) { i ->
                if (i % 10 == 0) {
                    registerPlayerPlaceholder("%clan_tag_$i%", 10000) { player ->
                        renderClanTag(
                            player,
                            i
                        )
                    }
                }
            }
        }
    }


    private fun renderClanTag(player: TabPlayer, minSize: Int = 0): String {
        val velocityPlayer = player.player as Player
        val clan = velocityPlayer.findClan() ?: return ""
        val clanTag = clan.tag

        if ((clanTag.isEmpty() || clan.members.size < minSize) && clanTag != "SLNE") {
            return ""
        }

        val clanTagComponent = Component.text()
        clanTagComponent.appendSpace()
        clanTagComponent.append(Component.text("[", NamedTextColor.DARK_GRAY))
        clanTagComponent.append(Component.text(clanTag.uppercase(), NamedTextColor.YELLOW))
        clanTagComponent.append(Component.text("]", NamedTextColor.DARK_GRAY))

        return LegacyComponentSerializer.legacySection().serialize(clanTagComponent.build())
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        eventManager.unregisterListeners(this)
    }

    companion object {
        lateinit var instance: VelocityClanPlugin
    }

}