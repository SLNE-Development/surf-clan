package dev.slne.clan.velocity.placeholder

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.velocity.clanConfigHolder
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import io.github.miniplaceholders.api.Expansion
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Tag

val placeholderManager = PlaceholderManager()

class PlaceholderManager {
    fun registerPlaceholders() {
        Expansion.builder("clan")
            .filter(Player::class.java)
            .audiencePlaceholder("name") { audience, _, _ ->
                val player = audience as Player
                val clan = player.findClan()
                Tag.inserting(text(clan?.name ?: ""))
            }
            .audiencePlaceholder("tag_raw") { audience, _, _ ->
                val player = audience as Player
                val clan = player.findClan()
                Tag.inserting(text(clan?.tag ?: ""))
            }
            .audiencePlaceholder("tag") { audience, queue, _ ->
                val player = audience as Player
                val minSize = queue.peek()?.asInt()?.orElseGet { 0 } ?: 0
                Tag.inserting(renderClanTag(player, minSize = minSize))
            }
            .audiencePlaceholder("tag_space") { audience, queue, _ ->
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
        val clan = player.findClan() ?: return Component.empty()
        val clanTag = clan.tag

        val whitelistedClanTags = clanConfigHolder.config.whitelistedTags
        val whitelistPermission = "surf.clan.tag.bypass"

        if ((clanTag.isEmpty() || clan.members.size < minSize) && clanTag !in whitelistedClanTags && !player.hasPermission(
                whitelistPermission
            )
        ) {
            return Component.empty()
        }

        val translatedClanTag = clan.getTranslatedClanTag()

        return text(if (space) " $translatedClanTag" else translatedClanTag)
    }
}