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
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import me.neznamy.tab.api.TabAPI
import me.neznamy.tab.api.TabPlayer
import me.neznamy.tab.api.event.plugin.TabLoadEvent
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
                "%clan_tag_single_arrow%",
                10000
            ) { player -> renderClanTag(player) }


            registerPlayerPlaceholder(
                "%clan_tag_double_arrow%",
                10000
            ) { player -> renderClanTag(player, doubleArrow = true) }


            registerPlayerPlaceholder(
                "%clan_tag_single_arrow_space%",
                10000
            ) { player -> renderClanTag(player, space = true) }


            registerPlayerPlaceholder(
                "%clan_tag_double_arrow_space%",
                10000
            ) { player -> renderClanTag(player, doubleArrow = true, space = true) }

            repeat(101) { i ->
                if (i % 10 == 0) {
                    registerPlayerPlaceholder("%clan_tag_${i}_single_arrow%", 10000) { player ->
                        renderClanTag(
                            player,
                            i
                        )
                    }

                    registerPlayerPlaceholder("%clan_tag_${i}_double_arrow%", 10000) { player ->
                        renderClanTag(
                            player,
                            i,
                            doubleArrow = true
                        )
                    }

                    registerPlayerPlaceholder(
                        "%clan_tag_${i}_single_arrow_space%",
                        10000
                    ) { player ->
                        renderClanTag(
                            player,
                            i,
                            space = true
                        )
                    }

                    registerPlayerPlaceholder(
                        "%clan_tag_${i}_double_arrow_space%",
                        10000
                    ) { player ->
                        renderClanTag(
                            player,
                            i,
                            doubleArrow = true,
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
        doubleArrow: Boolean = false
    ): String {
        val velocityPlayer = player.player as Player
        val clan = velocityPlayer.findClan(getBean<ClanService>()) ?: return ""
        val clanTag = clan.tag

        val whitelistedClanTags = listOf("SLNE", "CXN", "SPDY")

        if ((clanTag.isEmpty() || clan.members.size < minSize) && clanTag !in whitelistedClanTags) {
            return ""
        }

//        val clanTagComponent = buildText {
//            darkSpacer("[")
//            append(Component.text(clanTag.uppercase(), NamedTextColor.YELLOW))
//            darkSpacer("]")
//        }
        val clanTagComponent = buildText {
            if (space) {
                appendSpace()
            }

            spacer(if (doubleArrow) "<<" else "<")

            val tag = clanTag.uppercase()
            if (tag.contains("[0-9]".toRegex())) {
                append(Component.text(clanTag, NamedTextColor.YELLOW))
            } else {
                append(Component.text(clanTag.toSmallCaps(), NamedTextColor.YELLOW))
            }

            spacer(if (doubleArrow) ">>" else ">")
        }

        return LegacyComponentSerializer.legacySection().serialize(clanTagComponent)
    }

    @Subscribe
    fun onProxyShutdown(event: ProxyShutdownEvent) {
        eventManager.unregisterListeners(this)
    }

    companion object {
        lateinit var instance: VelocityClanPlugin
    }

}