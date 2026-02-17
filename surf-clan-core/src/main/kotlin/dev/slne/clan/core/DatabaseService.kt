package dev.slne.clan.core

import dev.slne.surf.surfapi.core.api.util.requiredService


interface DatabaseService {
    suspend fun createTables()
    fun disconnect()

    companion object {
        val instance = requiredService<DatabaseService>()
    }
}