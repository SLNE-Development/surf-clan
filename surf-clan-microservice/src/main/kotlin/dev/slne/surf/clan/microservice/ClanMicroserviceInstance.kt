package dev.slne.surf.clan.microservice

import com.google.auto.service.AutoService
import dev.slne.surf.clan.core.ClanInstance
import java.nio.file.Path

@AutoService(ClanInstance::class)
class ClanMicroserviceInstance : ClanInstance {
    override val dataPath: Path get() = clanMicroservice.configPath
}