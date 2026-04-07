package dev.slne.surf.clan.microservice.handler.invite

import dev.slne.surf.clan.core.protocol.invite.create.CreateClanInviteRequestPacket
import dev.slne.surf.clan.core.protocol.invite.create.CreateClanInviteResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanInviteRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object CreateClanInviteHandler {

    @RabbitHandler
    fun handleCreateClanInvite(request: CreateClanInviteRequestPacket) {
        val (clanID, invitee, invitedBy) = request

        request.launch {
            val result = ClanInviteRepository.createInvite(
                clanID = clanID,
                invitee = invitee,
                invitedBy = invitedBy
            )

            request.respond(CreateClanInviteResponsePacket(result))
        }
    }
}