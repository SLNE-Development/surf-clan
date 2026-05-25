package dev.slne.surf.clan.core.rpc

import dev.slne.surf.clan.core.player.ClanPlayerImpl
import dev.slne.surf.rabbitmq.api.rpc.RpcService
import java.util.*

@RpcService
interface ClanPlayerRpcService {

    suspend fun findClanPlayerByUuid(uuid: UUID): ClanPlayerImpl
    suspend fun updateAcceptsClanInvites(playerID: ULong, acceptsClanInvites: Boolean): Boolean

}