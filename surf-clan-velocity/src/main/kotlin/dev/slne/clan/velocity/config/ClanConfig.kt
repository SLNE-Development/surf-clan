package dev.slne.clan.velocity.config

import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.config.manager.SpongeConfigManager
import dev.slne.surf.surfapi.core.api.config.surfConfigApi
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ClanConfig(
    val whitelistedTags: List<String> = emptyList(),
) {
    class ClanConfigHolder {
        private val configManager: SpongeConfigManager<ClanConfig>

        init {
            surfConfigApi.createSpongeYmlConfig(
                ClanConfig::class.java,
                plugin.dataPath,
                "config.yml"
            )
            configManager = surfConfigApi.getSpongeConfigManagerForConfig(
                ClanConfig::class.java
            )
            reload()
        }

        fun reload() {
            configManager.reloadFromFile()
        }

        val config get() = configManager.config
    }
}