package dev.slne.clan.core.service

import dev.slne.clan.core.player.CoreClanPlayer
import dev.slne.clan.core.repository.ClanPlayerRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.util.*

@Service
class ClanPlayerService(private val nameCacheRepository: ClanPlayerRepository) {

    suspend fun findClanPlayerByName(name: String): CoreClanPlayer? = withContext(Dispatchers.IO) {
        nameCacheRepository.findFirstByUsername(name)
    }

    suspend fun findClanPlayerByUuid(uuid: UUID): CoreClanPlayer? = withContext(Dispatchers.IO) {
        nameCacheRepository.findFirstByUuid(uuid)
    }

    suspend fun save(clanPlayer: CoreClanPlayer): CoreClanPlayer = withContext(Dispatchers.IO) {
        nameCacheRepository.save(clanPlayer)
    }

}