package dev.slne.clan.velocity.placeholder

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import io.github.miniplaceholders.api.Expansion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Tag
import org.springframework.stereotype.Component as SpringComponent

@SpringComponent
class PlaceholderManager(private val clanService: ClanService) {

    fun registerPlaceholders() {

        Expansion.builder("clan")
            .filter(Player::class.java)
            .audiencePlaceholder("name") { audience, queue, ctx ->
                val player = audience as Player
                val clan = player.findClan(clanService)
                Tag.inserting(text(clan?.name ?: ""))
            }
            .audiencePlaceholder("tag_raw") { audience, queue, ctx ->
                val player = audience as Player
                val clan = player.findClan(clanService)
                Tag.inserting(text(clan?.tag ?: ""))
            }
            .audiencePlaceholder("tag") { audience, queue, ctx ->
                val player = audience as Player
                val minSize = queue.peek()?.asInt()?.orElseGet { 0 } ?: 0
                Tag.inserting(renderClanTag(player, minSize = minSize))
            }
            .audiencePlaceholder("tag_space") { audience, queue, ctx ->
                val player = audience as Player
                val minSize = queue.peek()?.asInt()?.orElseGet { 0 } ?: 0
                Tag.inserting(renderClanTag(player, minSize = minSize, space = true))
            }
            .build()
            .register()
    }


    private fun renderClanTag(
        player: Player,
        minSize: Int = 0,
        space: Boolean = false,
    ): Component {
        val clan = player.findClan(clanService) ?: return Component.empty()
        val clanTag = clan.tag

        val whitelistedClanTags = listOf("SLNE", "CXN", "SPDY")
        val whitelistPermission = "surf.clan.tag.bypass"

        if ((clanTag.isEmpty() || clan.members.size < minSize) && clanTag !in whitelistedClanTags && !player.hasPermission(
                whitelistPermission
            )
        ) {
            return Component.empty()
        }

        val translatedClanTag = clan.getTranslatedClanTag() ?: return text("ERROR", Colors.ERROR)

        return text(if (space) " $translatedClanTag" else translatedClanTag)
    }
}