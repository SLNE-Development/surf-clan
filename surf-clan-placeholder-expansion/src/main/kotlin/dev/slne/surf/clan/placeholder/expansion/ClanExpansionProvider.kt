package dev.slne.surf.clan.placeholder.expansion

import dev.slne.clan.api.clan.Clan
import dev.slne.surf.clan.placeholder.expansion.ClanDataCache.CachedClanData
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import dev.slne.surf.surfapi.core.api.messages.adventure.uuid
import io.github.miniplaceholders.api.Expansion
import io.github.miniplaceholders.api.utils.Tags
import net.kyori.adventure.text.minimessage.tag.Tag

object ClanExpansionProvider {

    fun provideExpansion(): Expansion {
        Clan.registerListener(ClanDataCache)

        return Expansion.builder("clan")
            .author("Ammo, red & twisti")
            .version("1.2.0")
            .audiencePlaceholder("name") { audience, _, _ ->
                when (val data = ClanDataCache.getData(audience.uuid())) {
                    CachedClanData.Empty -> Tags.EMPTY_TAG
                    is CachedClanData.Loaded -> Tag.inserting(text(data.name))
                }
            }
            .audiencePlaceholder("tag_raw") { audience, _, _ ->
                when (val data = ClanDataCache.getData(audience.uuid())) {
                    CachedClanData.Empty -> Tags.EMPTY_TAG
                    is CachedClanData.Loaded -> Tag.inserting(text(data.tag))
                }
            }
            .audiencePlaceholder("tag") { audience, queue, _ ->
                when (val data = ClanDataCache.getData(audience.uuid())) {
                    CachedClanData.Empty -> Tags.EMPTY_TAG
                    is CachedClanData.Loaded -> Tag.inserting(data.renderedClanTag)
                }
            }
            .build()
    }
}