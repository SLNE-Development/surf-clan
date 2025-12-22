package dev.slne.clan.core.service

import dev.slne.clan.api.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

val clanPlayerService = requiredService<ClanPlayerService>()

interface ClanPlayerService {
    suspend fun findClanPlayerByName(name: String): ClanPlayer?
    suspend fun findClanPlayerByUuid(uuid: UUID): ClanPlayer?
    suspend fun save(clanPlayer: ClanPlayer): ClanPlayer
}