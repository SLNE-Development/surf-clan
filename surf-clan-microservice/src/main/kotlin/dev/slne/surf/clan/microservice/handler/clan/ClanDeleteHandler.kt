package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.delete.DeleteClanRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.coroutines.launch

object ClanDeleteHandler {

    @RabbitHandler
    fun handleDeleteClan(request: DeleteClanRequestPacket) {
        request.launch {
            val deleted = ClanRepository.delete(request.clanID)
            request.respond(PrimitiveResponse.BooleanResponsePacket(deleted))
        }
    }
}