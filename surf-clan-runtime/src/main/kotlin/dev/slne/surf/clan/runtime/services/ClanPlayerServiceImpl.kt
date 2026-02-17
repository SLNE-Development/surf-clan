package dev.slne.surf.clan.runtime.services

import com.google.auto.service.AutoService
import dev.slne.clan.api.player.ClanPlayerService
import dev.slne.clan.core.player.ClanPlayerImpl
import dev.slne.clan.core.player.CoreClanPlayerService
import dev.slne.surf.clan.runtime.db.repository.ClanPlayerRepository
import dev.slne.clan.core.redis.RedisService
import java.util.UUID
import kotlin.time.Duration.Companion.minutes

@AutoService(ClanPlayerService::class)
class ClanPlayerServiceImpl : CoreClanPlayerService {
    private val cache = RedisService.get().redisApi.createSimpleCache<UUID, ClanPlayerImpl>(
        RedisService.namespaced("clan_player_cache"),
        30.minutes
    )

    override suspend fun invalidateCaches() {
        cache.invalidateAll()
    }

    override suspend fun findByUuid(uuid: UUID): ClanPlayerImpl {
        return cache.cachedOrLoad(uuid) {
            ClanPlayerRepository.findOrCreateByUuid(uuid)
        }
    }

    override suspend fun changeAcceptsClanInvites(playerImpl: ClanPlayerImpl, acceptsClanInvites: Boolean): Boolean {
        val changed = ClanPlayerRepository.changeAcceptsClanInvites(playerImpl.id, acceptsClanInvites)
        if (changed) {
            cache.invalidate(playerImpl.uuid)
        }

        return changed
    }

    companion object {
        fun get() = ClanPlayerService.instance as ClanPlayerServiceImpl
    }
}