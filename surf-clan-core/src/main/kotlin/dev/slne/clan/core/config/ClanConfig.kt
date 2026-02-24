package dev.slne.clan.core.config

import dev.slne.clan.core.ClanInstance
import dev.slne.surf.surfapi.core.api.config.SpongeYmlConfigClass
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ClanConfig(
    val whitelistedTags: List<String> = emptyList(), // TODO: save in db
    val autoDisband: AutoDisbandConfig = AutoDisbandConfig()
) {
    @ConfigSerializable
    data class AutoDisbandConfig(
        val enabled: Boolean = false,
        val inactivityDays: Int = 90,
        val checkIntervalHours: Int = 24
    )

    companion object : SpongeYmlConfigClass<ClanConfig>(
        ClanConfig::class.java,
        ClanInstance.get().dataPath,
        "config.yml"
    )
}