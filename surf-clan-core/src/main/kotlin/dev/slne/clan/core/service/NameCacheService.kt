package dev.slne.clan.core.service

import dev.slne.clan.core.name.CoreNameCache
import dev.slne.clan.core.repository.ClanNameCacheRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import java.util.*

@Service
class NameCacheService(private val nameCacheRepository: ClanNameCacheRepository) {

    suspend fun findNameByUuid(uuid: UUID): String? = withContext(Dispatchers.IO) {
        nameCacheRepository.findFirstByUuid(uuid)?.username
    }

    suspend fun findUuidByName(name: String): UUID? = withContext(Dispatchers.IO) {
        nameCacheRepository.findFirstByUsername(name)?.uuid
    }

    suspend fun createNameCache(uuid: UUID, username: String) = withContext(Dispatchers.IO) {
        nameCacheRepository.save(CoreNameCache(username = username, uuid = uuid))

    }

    suspend fun updateNameCache(uuid: UUID, username: String) = withContext(Dispatchers.IO) {
        val nameCache = nameCacheRepository.findFirstByUuid(uuid)

        if (nameCache != null) {
            nameCache.username = username

            nameCacheRepository.save(nameCache)
        }
    }

}