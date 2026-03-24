package dev.slne.surf.clan.microservice.handler.invite

import dev.slne.surf.clan.core.protocol.invite.findPendingByInvitedAndClanName.FindPendingInviteByInvitedPlayerAndClanNameRequestPacket
import dev.slne.surf.clan.core.protocol.invite.findPendingByInvitedAndClanName.FindPendingInviteByInvitedPlayerAndClanNameResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanInviteRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindPendingInviteByInvitedPlayerAndClanNameHandler {

    @RabbitHandler
    fun handleFindPendingInviteByInvitedPlayerAndClanName(request: FindPendingInviteByInvitedPlayerAndClanNameRequestPacket) {
        val (invited, clanName) = request

        request.launch {
            val invite = ClanInviteRepository.getPendingInviteByPlayerAndClanName(
                invited = invited,
                clanName = clanName
            )

            request.respond(FindPendingInviteByInvitedPlayerAndClanNameResponsePacket(invite))
        }
    }
}

