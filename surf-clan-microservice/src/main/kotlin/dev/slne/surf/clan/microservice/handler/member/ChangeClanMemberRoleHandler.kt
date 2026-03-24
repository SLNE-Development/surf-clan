package dev.slne.surf.clan.microservice.handler.member

import dev.slne.surf.clan.core.protocol.member.changeRole.ChangeClanMemberRoleRequestPacket
import dev.slne.surf.clan.microservice.db.repository.ClanMemberRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.coroutines.launch

object ChangeClanMemberRoleHandler {

    @RabbitHandler
    fun handleChangeClanMemberRole(request: ChangeClanMemberRoleRequestPacket) {
        val (memberID, role) = request

        request.launch {
            val changed = ClanMemberRepository.changeRole(memberID, role)
            request.respond(PrimitiveResponse.BooleanResponsePacket(changed))
        }
    }
}

