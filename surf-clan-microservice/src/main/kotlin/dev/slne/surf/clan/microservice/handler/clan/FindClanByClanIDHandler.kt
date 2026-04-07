package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.OptionalClanResponsePacket
import dev.slne.surf.clan.core.protocol.clan.findByID.FindClanByClanIDRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindClanByClanIDHandler {

    @RabbitHandler
    fun handleFindClanByClanID(request: FindClanByClanIDRequestPacket) {
        request.launch {
            val clan = ClanRepository.findClanByID(request.clanID)
            request.respond(OptionalClanResponsePacket(clan))
        }
    }
}