package dev.slne.clan.core

import dev.slne.surf.surfapi.core.api.util.requiredService
import java.nio.file.Path

val databaseLoader = requiredService<DatabaseLoader>()

interface DatabaseLoader {
    fun connect(path: Path)
    fun createTables()
    fun disconnect()
}