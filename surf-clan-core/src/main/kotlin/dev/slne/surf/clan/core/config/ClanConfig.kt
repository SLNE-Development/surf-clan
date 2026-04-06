package dev.slne.surf.clan.core.config

import dev.slne.surf.api.core.config.SpongeYmlConfigClass
import dev.slne.surf.clan.core.ClanInstance
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ClanConfig(
    val whitelistedTags: List<String> = emptyList() // TODO: save in db
) {
    companion object : SpongeYmlConfigClass<ClanConfig>(
        ClanConfig::class.java,
        ClanInstance.dataPath,
        "config.yml"
    )
}