package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.updateNameAndTag.UpdateClanNameAndTagRequestPacket
import dev.slne.surf.clan.core.protocol.clan.updateNameAndTag.UpdateClanNameAndTagResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler

object UpdateClanNameAndTagRequestHandler {

    @RabbitHandler
    suspend fun handleUpdateClanNameAndTagRequest(request: UpdateClanNameAndTagRequestPacket) {
        val (clanID, name, tag) = request
        val result = ClanRepository.updateClanNameAndTag(clanID, name, tag)
        request.respond(UpdateClanNameAndTagResponsePacket(result))
    }
}