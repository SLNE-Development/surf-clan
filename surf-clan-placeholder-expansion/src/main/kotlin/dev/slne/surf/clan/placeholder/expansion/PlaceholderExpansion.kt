package dev.slne.surf.clan.placeholder.expansion

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.api.surfClanApi
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import io.github.miniplaceholders.api.Expansion
import io.github.miniplaceholders.api.provider.ExpansionProvider
import io.github.miniplaceholders.api.provider.LoadRequirement
import io.github.miniplaceholders.api.types.Platform
import net.kyori.adventure.text.minimessage.tag.Tag

class PlaceholderExpansion : ExpansionProvider {
    override fun provideExpansion(): Expansion =
        Expansion.builder("clan")
            .author("Ammo & red")
            .version("1.2.0")
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
                val minSize = queue.peek()?.asInt()?.orElseGet { 10 } ?: 10
                Tag.inserting(surfClanApi.renderClanTag(player, minSize = minSize))
            }
            .build()

    override fun loadRequirement(): LoadRequirement = LoadRequirement.platform(Platform.VELOCITY)
}