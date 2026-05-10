package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.findAllWithoutMembersSortByMemberCount.FindAllClansWithoutMembersSortByMemberCountRequestPacket
import dev.slne.surf.clan.core.protocol.clan.findAllWithoutMembersSortByMemberCount.FindAllClansWithoutMembersSortByMemberCountResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler

object FindAllClansWithoutMembersSortByMemberCountHandler {

    @RabbitHandler
    suspend fun handleFindAllClansWithoutMembersSortByMemberCount(request: FindAllClansWithoutMembersSortByMemberCountRequestPacket) {
        val fetched = ClanRepository.fetchAllClansWithoutMembersSortByMemberCount()
        request.respond(FindAllClansWithoutMembersSortByMemberCountResponsePacket(fetched))
    }
}