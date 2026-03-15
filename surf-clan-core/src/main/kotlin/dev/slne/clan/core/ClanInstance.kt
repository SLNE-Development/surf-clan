package dev.slne.clan.core

import dev.slne.clan.core.clan.CoreClanService
import dev.slne.clan.core.redis.RedisService
import dev.slne.surf.surfapi.core.api.util.requiredService
import org.jetbrains.annotations.MustBeInvokedByOverriders
import java.nio.file.Path

private val instance = requiredService<ClanInstance>()

interface ClanInstance {
    val dataPath: Path

    @MustBeInvokedByOverriders
    suspend fun load() {
        DatabaseService.createTables()
        CoreClanService.init()
        RedisService.get().connect()
    }

    @MustBeInvokedByOverriders
    suspend fun enable() {

    }

    @MustBeInvokedByOverriders
    suspend fun disable() {
        RedisService.get().disconnect()
        DatabaseService.disconnect()
    }

    companion object : ClanInstance by instance {
        val INSTANCE get() = instance
    }
}