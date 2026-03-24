package dev.slne.surf.clan.microservice.handler.invite

import dev.slne.surf.clan.core.protocol.invite.accept.AcceptClanInviteRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanInviteRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.coroutines.launch

object AcceptClanInviteHandler {

    @RabbitHandler
    fun handleAcceptClanInvite(request: AcceptClanInviteRequestPacket) {
        val (inviteID, invitee, invitedBy) = request

        request.launch {
            val result = ClanInviteRepository.acceptInvite(
                inviteID = inviteID,
                invitee = invitee,
                invitedBy = invitedBy
            )

            request.respond(PrimitiveResponse.BooleanResponsePacket(result))
        }
    }
}