package dev.slne.surf.clan.microservice.handler.player

import dev.slne.surf.clan.core.protocol.player.updateAccepsClanInvites.UpdateClanPlayerAcceptsInvitesRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanPlayerRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.coroutines.launch

object UpdateClanPlayerAcceptsInvitesHandler {

    @RabbitHandler
    fun handleUpdateClanPlayerAcceptsInvites(request: UpdateClanPlayerAcceptsInvitesRequestPacket) {
        val (playerID, acceptsClanInvites) = request

        request.launch {
            val updated = ClanPlayerRepository.changeAcceptsClanInvites(playerID, acceptsClanInvites)
            request.respond(PrimitiveResponse.BooleanResponsePacket(updated))
        }
    }
}

