package dev.slne.clan.core

import dev.slne.clan.api.util.InternalClanApi
import dev.slne.surf.surfapi.core.api.util.requiredService

private val service = requiredService<DatabaseService>()

@InternalClanApi
interface DatabaseService {
    suspend fun createTables()
    fun disconnect()

    companion object : DatabaseService by service {
        val INSTANCE get() = service
    }
}