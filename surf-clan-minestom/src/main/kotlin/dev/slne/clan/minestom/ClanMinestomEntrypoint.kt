package dev.slne.clan.minestom

import com.google.inject.Inject
import com.google.inject.Singleton
import dev.slne.minestom.lobby.api.plugin.MinestomPluginEntrypoint
import dev.slne.minestom.lobby.api.plugin.annotation.DataDirectory
import dev.slne.surf.clan.core.ClanInstance
import java.nio.file.Path

@Singleton
class ClanMinestomEntrypoint @Inject constructor(
    @DataDirectory path: Path
) : MinestomPluginEntrypoint {

    init {
        dataPath = path
    }

    override suspend fun start() {
        ClanInstance.load()
        ClanInstance.enable()
    }

    override suspend fun stop() {
        ClanInstance.disable()
    }

    companion object {
        lateinit var dataPath: Path
    }
}
