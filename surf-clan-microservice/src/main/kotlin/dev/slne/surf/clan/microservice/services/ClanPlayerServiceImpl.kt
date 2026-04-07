package dev.slne.surf.clan.microservice.services

import com.google.auto.service.AutoService
import dev.slne.clan.api.player.ClanPlayerService
import dev.slne.surf.clan.core.player.ClanPlayerImpl
import dev.slne.surf.clan.core.player.CoreClanPlayerService
import java.util.*

@AutoService(ClanPlayerService::class)
class ClanPlayerServiceImpl : CoreClanPlayerService {
    override suspend fun invalidateCaches() = Unit

    override suspend fun findByUuid(uuid: UUID): ClanPlayerImpl {
        throw NotImplementedError()
    }

    override suspend fun changeAcceptsClanInvites(
        playerImpl: ClanPlayerImpl,
        acceptsClanInvites: Boolean
    ): Boolean {
        throw NotImplementedError()
    }
}