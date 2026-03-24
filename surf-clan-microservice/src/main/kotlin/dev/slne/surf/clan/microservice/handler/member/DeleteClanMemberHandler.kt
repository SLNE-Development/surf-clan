package dev.slne.surf.clan.microservice.handler.member

import dev.slne.surf.clan.core.protocol.member.delete.DeleteClanMemberRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanMemberRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.coroutines.launch

object DeleteClanMemberHandler {

    @RabbitHandler
    fun handleDeleteClanMember(request: DeleteClanMemberRequestPacket) {
        val (clanID, playerUuid) = request

        request.launch {
            val deleted = ClanMemberRepository.deleteMember(clanID, playerUuid)
            request.respond(PrimitiveResponse.BooleanResponsePacket(deleted))
        }
    }
}

