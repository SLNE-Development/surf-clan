package dev.slne.surf.clan.core.config

import dev.slne.surf.api.core.config.SpongeYmlConfigClass
import dev.slne.surf.clan.core.ClanInstance
import org.spongepowered.configurate.objectmapping.ConfigSerializable

@ConfigSerializable
data class ClanConfig(
    val whitelistedTags: List<String> = emptyList(), // TODO: save in db
    val cleanup: Cleanup = Cleanup()
) {
    /**
     * Settings for the task that deletes clans nobody uses anymore.
     *
     * Only read by the microservice — it is the only place the task runs.
     */
    @ConfigSerializable
    data class Cleanup(
        val enabled: Boolean = true,

        /**
         * Log what would be deleted without deleting it.
         *
         * Defaults to on: a freshly deployed microservice must not remove clans before someone has
         * looked at the list and decided.
         */
        val dryRun: Boolean = true,

        val intervalMinutes: Int = 60
    )

    companion object : SpongeYmlConfigClass<ClanConfig>(
        ClanConfig::class.java,
        ClanInstance.dataPath,
        "config.yml"
    )
}