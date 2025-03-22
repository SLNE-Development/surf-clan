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
import me.neznamy.tab.api.TabPlayer
import me.neznamy.tab.api.event.plugin.TabLoadEvent
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
    }

    @Subscribe(order = PostOrder.LATE)
    fun onProxyInitialization(event: ProxyInitializeEvent) {
        ClanCommand(getBean<ClanService>(), getBean<ClanPlayerService>()).register()
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

                velocityPlayer.findClan(getBean<ClanService>())?.name ?: ""
            }

            registerPlayerPlaceholder(
                "%clan_tag_raw%",
                10000
            ) { player ->
                val velocityPlayer = player.player as Player

                velocityPlayer.findClan(getBean<ClanService>())?.tag ?: ""
            }

            registerPlayerPlaceholder(
                "%clan_tag%",
                10000
            ) { player -> renderClanTag(player) }


            registerPlayerPlaceholder(
                "%clan_tag_space%",
                10000
            ) { player -> renderClanTag(player, space = true) }

            repeat(101) { i ->
                if (i % 10 == 0) {
                    registerPlayerPlaceholder("%clan_tag_${i}%", 10000) { player ->
                        renderClanTag(
                            player,
                            i,
                        )
                    }

                    registerPlayerPlaceholder(
                        "%clan_tag_${i}_space%", 10000
                    ) { player ->
                        renderClanTag(
                            player,
                            i,
                            space = true
                        )
                    }
                }
            }
        }
    }

    private fun renderClanTag(
        player: TabPlayer,
        minSize: Int = 0,
        space: Boolean = false,
    ): String {
        val velocityPlayer = player.player as Player
        val clan = velocityPlayer.findClan(getBean<ClanService>()) ?: return ""
        val clanTag = clan.tag

        val whitelistedClanTags = listOf("SLNE", "CXN", "SPDY")
        val whitelistPermission = "surf.clan.tag.bypass"

        if ((clanTag.isEmpty() || clan.members.size < minSize) && clanTag !in whitelistedClanTags && !velocityPlayer.hasPermission(
                whitelistPermission
            )
        ) {
            return ""
        }

        val translatedClanTag = clan.getTranslatedClanTag() ?: return "ERROR"

        return if (space) " $translatedClanTag" else translatedClanTag
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        eventManager.unregisterListeners(this)
    }

    companion object {
        lateinit var instance: VelocityClanPlugin
    }

}