package dev.slne.surf.clan.placeholder.expansion

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.api.surfClanApi
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import io.github.miniplaceholders.api.Expansion
import io.github.miniplaceholders.api.provider.ExpansionProvider
import io.github.miniplaceholders.api.provider.LoadRequirement
import io.github.miniplaceholders.api.types.Platform
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.Tag

class PlaceholderExpansion : ExpansionProvider {
    override fun provideExpansion(): Expansion =
        Expansion.builder("clan")
            .audiencePlaceholder("name") { audience, _, _ ->
                val player = audience as Player
                val clan = surfClanApi.findClan(player)
                Tag.inserting(text(clan?.name ?: ""))
            }
            .audiencePlaceholder("tag_raw") { audience, _, _ ->
                val player = audience as Player
                val clan = surfClanApi.findClan(player)
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

    override fun loadRequirement(): LoadRequirement = LoadRequirement.platform(Platform.VELOCITY)

    private fun renderClanTag(
        player: Player,
        minSize: Int = 0,
        space: Boolean = false,
    ): Component {
        val clan = surfClanApi.findClan(player) ?: return Component.empty()
        val clanTag = clan.tag

        val whitelistedClanTags = surfClanApi.getWhitelistedClans()
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