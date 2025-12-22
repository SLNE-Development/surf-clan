package dev.slne.surf.clan.fallback.service

import com.google.auto.service.AutoService
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.surf.clan.fallback.repository.clanPlayerRepository
import net.kyori.adventure.util.Services
import java.util.*

@AutoService(ClanPlayerService::class)
class ClanPlayerServiceImpl : ClanPlayerService, Services.Fallback {
    override suspend fun findClanPlayerByName(name: String): ClanPlayer? =
        clanPlayerRepository.findFirstByUsername(name)

    override suspend fun findClanPlayerByUuid(uuid: UUID): ClanPlayer? =
        clanPlayerRepository.findFirstByUuid(uuid)

    override suspend fun save(clanPlayer: ClanPlayer): ClanPlayer =
        clanPlayerRepository.save(clanPlayer)
}