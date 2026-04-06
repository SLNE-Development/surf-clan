package dev.slne.surf.clan.core

import dev.slne.surf.api.core.util.requiredService
import dev.slne.surf.clan.core.clan.CoreClanService
import org.jetbrains.annotations.MustBeInvokedByOverriders
import java.nio.file.Path

private val instance = requiredService<ClanInstance>()

interface ClanInstance {
    val dataPath: Path

    @MustBeInvokedByOverriders
    suspend fun load() {
        CoreClanService.init()
    }

    @MustBeInvokedByOverriders
    suspend fun enable() {
    }

    @MustBeInvokedByOverriders
    suspend fun disable() {
    }

    companion object : ClanInstance by instance {
        val INSTANCE get() = instance
    }
}