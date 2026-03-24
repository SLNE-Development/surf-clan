package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.OptionalClanResponsePacket
import dev.slne.surf.clan.core.protocol.clan.findByTag.FindClanByTagRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindClanByTagHandler {

    @RabbitHandler
    fun handleFindClanByTag(request: FindClanByTagRequestPacket) {
        request.launch {
            val clan = ClanRepository.findClanByTag(request.tag)
            request.respond(OptionalClanResponsePacket(clan))
        }
    }
}