package dev.slne.clan.core

import dev.slne.clan.core.clan.CoreClanService
import dev.slne.clan.core.config.ClanConfig
import dev.slne.clan.core.redis.RedisService
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.requiredService
import kotlinx.coroutines.*
import org.jetbrains.annotations.MustBeInvokedByOverriders
import java.nio.file.Path
import kotlin.time.Duration.Companion.hours

abstract class ClanInstance {

    abstract val dataPath: Path

    private val scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    private var autoDisbandJob: Job? = null

    @MustBeInvokedByOverriders
    open suspend fun load() {
        DatabaseService.instance.createTables()
        CoreClanService.init()
        RedisService.get().connect()
    }

    @MustBeInvokedByOverriders
    open suspend fun enable() {
        startAutoDisbandTask()
    }

    @MustBeInvokedByOverriders
    open suspend fun disable() {
        autoDisbandJob?.cancel()
        scope.cancel()
        RedisService.get().disconnect()
        DatabaseService.instance.disconnect()
    }

    private fun startAutoDisbandTask() {
        val config = ClanConfig.getConfig().autoDisband
        if (!config.enabled) return

        val checkIntervalMillis = config.checkIntervalHours.hours.inWholeMilliseconds

        autoDisbandJob = scope.launch {
            while (isActive) {
                try {
                    val count = CoreClanService.disbandInactiveClans(config.inactivityDays)
                    if (count > 0) {
                        log.atInfo().log("Auto-disbanded %d inactive clan(s)", count)
                    }
                } catch (e: Exception) {
                    log.atWarning()
                        .withCause(e)
                        .log("Failed to auto-disband inactive clans")
                }
                delay(checkIntervalMillis)
            }
        }
    }

    companion object {
        private val log = logger()
        val instance = requiredService<ClanInstance>()
        fun get() = instance
    }
}