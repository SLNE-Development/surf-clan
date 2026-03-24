package dev.slne.surf.clan.microservice.handler.invite

import dev.slne.surf.clan.core.protocol.invite.delete.DeleteInviteClanInviteRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanInviteRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.coroutines.launch

object DeleteInviteClanInviteHandler {

    @RabbitHandler
    fun handleDeleteInviteClanInvite(request: DeleteInviteClanInviteRequestPacket) {
        val (clanID, invitee) = request

        request.launch {
            val deleted = ClanInviteRepository.deleteInvite(clanID, invitee)
            request.respond(PrimitiveResponse.BooleanResponsePacket(deleted))
        }
    }
}

