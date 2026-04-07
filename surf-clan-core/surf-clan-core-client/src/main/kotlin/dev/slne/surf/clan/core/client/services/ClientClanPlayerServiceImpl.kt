package dev.slne.surf.clan.core.client.services

import com.google.auto.service.AutoService
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.clan.api.player.ClanPlayerService
import dev.slne.surf.clan.core.client.rabbit.rabbitApi
import dev.slne.surf.clan.core.client.redis.RedisService
import dev.slne.surf.clan.core.player.ClanPlayerImpl
import dev.slne.surf.clan.core.player.CoreClanPlayerService
import dev.slne.surf.clan.core.protocol.player.findByUuid.FindClanPlayerByUuidRequestPacket
import dev.slne.surf.clan.core.protocol.player.updateAccepsClanInvites.UpdateClanPlayerAcceptsInvitesRequestPacket
import java.util.*
import kotlin.time.Duration.Companion.minutes

@AutoService(ClanPlayerService::class)
class ClientClanPlayerServiceImpl : CoreClanPlayerService {
    private val cache = RedisService.get().redisApi.createSimpleCache<UUID, ClanPlayerImpl>(
        RedisService.namespaced("clan_player_cache"),
        30.minutes
    )

    override suspend fun invalidateCaches() {
        cache.invalidateAll()
    }

    override suspend fun findByUuid(uuid: UUID): ClanPlayer {
        return cache.cachedOrLoad(uuid) {
            rabbitApi.sendRequest(FindClanPlayerByUuidRequestPacket(uuid)).player
        }
    }

    override suspend fun changeAcceptsClanInvites(
        playerImpl: ClanPlayerImpl,
        acceptsClanInvites: Boolean
    ): Boolean {
        val request = UpdateClanPlayerAcceptsInvitesRequestPacket(playerImpl.ID, acceptsClanInvites)
        val changed = rabbitApi.sendRequest(request).value

        if (changed) {
            cache.invalidate(playerImpl.uuid)
        }

        return changed
    }
}