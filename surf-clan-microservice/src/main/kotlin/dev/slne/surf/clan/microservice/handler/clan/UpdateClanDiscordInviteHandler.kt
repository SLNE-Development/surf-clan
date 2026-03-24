package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.updateDiscordInvite.UpdateClanDiscordInviteRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.coroutines.launch

object UpdateClanDiscordInviteHandler {

    @RabbitHandler
    fun handleUpdateClanDiscordInvite(request: UpdateClanDiscordInviteRequestPacket) {
        request.launch {
            val updated = ClanRepository.updateDiscordInvite(request.clanID, request.discordInvite)
            request.respond(PrimitiveResponse.BooleanResponsePacket(updated))
        }
    }
}