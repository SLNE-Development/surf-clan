package dev.slne.surf.clan.microservice.rpc

import dev.slne.surf.clan.core.player.ClanPlayerImpl
import dev.slne.surf.clan.core.rpc.ClanPlayerRpcService
import dev.slne.surf.clan.microservice.db.repository.ClanPlayerRepository
import java.util.*

object ClanPlayerRpcServiceImpl : ClanPlayerRpcService {

    override suspend fun findClanPlayerByUuid(uuid: UUID): ClanPlayerImpl {
        return ClanPlayerRepository.findOrCreateByUuid(uuid)
    }

    override suspend fun updateAcceptsClanInvites(
        playerID: ULong,
        acceptsClanInvites: Boolean
    ): Boolean {
        return ClanPlayerRepository.changeAcceptsClanInvites(playerID, acceptsClanInvites)
    }
}