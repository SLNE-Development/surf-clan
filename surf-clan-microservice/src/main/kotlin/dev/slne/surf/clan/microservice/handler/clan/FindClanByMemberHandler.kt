package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.OptionalClanResponsePacket
import dev.slne.surf.clan.core.protocol.clan.findByMember.FindClanByMemberRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindClanByMemberHandler {

    @RabbitHandler
    fun handleFindClanByMember(request: FindClanByMemberRequestPacket) {
        request.launch {
            val clan = ClanRepository.findClanByPlayer(request.memberUuid)
            request.respond(OptionalClanResponsePacket(clan))
        }
    }
}