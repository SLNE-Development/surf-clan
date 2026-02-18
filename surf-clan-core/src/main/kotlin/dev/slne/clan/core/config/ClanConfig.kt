package dev.slne.clan.core.config

import dev.slne.clan.core.ClanInstance
import dev.slne.surf.surfapi.core.api.config.SpongeYmlConfigClass
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ClanConfig(
    val whitelistedTags: List<String> = emptyList() // TODO: save in db
) {
    companion object : SpongeYmlConfigClass<ClanConfig>(
        ClanConfig::class.java,
        ClanInstance.get().dataPath,
        "config.yml"
    )
}