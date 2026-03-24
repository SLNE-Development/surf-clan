package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.OptionalClanResponsePacket
import dev.slne.surf.clan.core.protocol.clan.findByUuid.FindClanByUuidRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindClanByUuidHandler {

    @RabbitHandler
    fun handleFindClanByUuid(request: FindClanByUuidRequestPacket) {
        request.launch {
            val clan = ClanRepository.findClanByUuid(request.clanUuid)
            request.respond(OptionalClanResponsePacket(clan))
        }
    }
}