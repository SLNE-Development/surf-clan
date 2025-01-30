package dev.slne.clan.core.service

import com.github.benmanes.caffeine.cache.Caffeine
import dev.slne.clan.api.Clan
import dev.slne.clan.core.CoreClan
import dev.slne.clan.core.repository.ClanRepository
import jakarta.annotation.PostConstruct
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class ClanService(
    private val clanRepository: ClanRepository,
) {

    private val clanCache = Caffeine.newBuilder().build<UUID, CoreClan>()

    val clans get() = clanCache.asMap().values

    @PostConstruct
    fun initializeCache() = runBlocking {
        clanCache.invalidateAll()

        clanRepository.findAll().forEach { clanCache.put(it.uuid, it) }
    }

    fun findClanByTag(tag: String) = clans.find { it.tag.equals(tag, ignoreCase = true) }

    fun findClanByName(name: String) = clans.find { it.name.equals(name, ignoreCase = true) }

    fun findClanByMember(uuid: UUID) =
        clans.find { it.members.any { member -> member.uuid == uuid } }

    fun findInvitesByMember(memberUuid: UUID) = clans
        .flatMap { it.invites }.filter { it.invited == memberUuid }

    @Transactional
    suspend fun saveClan(clan: Clan) = withContext(Dispatchers.IO) {
        clanRepository.save(clan as CoreClan).also { clanCache.put(it.uuid, it) }
    }

    suspend fun deleteClan(clan: Clan) = withContext(Dispatchers.IO) {
        clanRepository.delete(clan as CoreClan)
        clanCache.invalidate(clan.uuid)
    }

    suspend fun createUnusedClanUuid(): UUID {
        var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (clans.any { it.uuid == uuid })

        return uuid
    }

}