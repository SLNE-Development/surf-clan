package dev.slne.clan.minestom

import com.google.auto.service.AutoService
import dev.slne.surf.clan.core.ClanInstance
import dev.slne.surf.clan.core.client.ClientClanInstance
import java.nio.file.Path

@AutoService(ClanInstance::class)
class MinestomClanInstance : ClientClanInstance() {
    override val dataPath: Path get() = ClanMinestomEntrypoint.dataPath
}
