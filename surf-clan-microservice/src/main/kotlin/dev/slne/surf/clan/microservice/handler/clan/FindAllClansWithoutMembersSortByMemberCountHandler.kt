package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.findAllWithoutMembersSortByMemberCount.FindAllClansWithoutMembersSortByMemberCountRequestPacket
import dev.slne.surf.clan.core.protocol.clan.findAllWithoutMembersSortByMemberCount.FindAllClansWithoutMembersSortByMemberCountResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindAllClansWithoutMembersSortByMemberCountHandler {

    @RabbitHandler
    fun handleFindAllClansWithoutMembersSortByMemberCount(request: FindAllClansWithoutMembersSortByMemberCountRequestPacket) {
        request.launch {
            val fetched = ClanRepository.fetchAllClansWithoutMembersSortByMemberCount()
            request.respond(FindAllClansWithoutMembersSortByMemberCountResponsePacket(fetched))
        }
    }
}