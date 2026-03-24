package dev.slne.surf.clan.microservice.handler.invite

import dev.slne.surf.clan.core.protocol.invite.findPendingByInvited.FindPendingInvitesByInvitedRequestPacket
import dev.slne.surf.clan.core.protocol.invite.findPendingByInvited.FindPendingInvitesByInvitedResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanInviteRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindPendingInvitesByInvitedHandler {

    @RabbitHandler
    fun handleFindPendingInvitesByInvited(request: FindPendingInvitesByInvitedRequestPacket) {
        val (invited) = request

        request.launch {
            val invites = ClanInviteRepository.getPendingInvitesByPlayer(invited)
            request.respond(FindPendingInvitesByInvitedResponsePacket(invites))
        }
    }
}

