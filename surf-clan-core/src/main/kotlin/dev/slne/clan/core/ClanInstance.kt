package dev.slne.clan.core

import dev.slne.clan.core.clan.CoreClanService
import dev.slne.clan.core.redis.RedisService
import dev.slne.surf.surfapi.core.api.util.requiredService
import org.jetbrains.annotations.MustBeInvokedByOverriders
import java.nio.file.Path

abstract class ClanInstance {

    abstract val dataPath: Path

    @MustBeInvokedByOverriders
    open suspend fun load() {
        DatabaseService.instance.createTables()
        CoreClanService.init()
        RedisService.get().connect()
    }

    @MustBeInvokedByOverriders
    open suspend fun enable() {

    }

    @MustBeInvokedByOverriders
    open suspend fun disable() {
        RedisService.get().disconnect()
        DatabaseService.instance.disconnect()
    }

    companion object {
        val instance = requiredService<ClanInstance>()
        fun get() = instance
    }
}