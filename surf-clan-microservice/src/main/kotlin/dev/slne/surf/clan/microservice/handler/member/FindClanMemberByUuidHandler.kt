package dev.slne.surf.clan.microservice.handler.member

import dev.slne.surf.clan.core.protocol.member.findByUuid.FindClanMemberByUuidRequestPacket
import dev.slne.surf.clan.core.protocol.member.findByUuid.FindClanMemberByUuidResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanMemberRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindClanMemberByUuidHandler {

    @RabbitHandler
    fun handleFindClanMemberByUuid(request: FindClanMemberByUuidRequestPacket) {
        val (uuid) = request

        request.launch {
            val member = ClanMemberRepository.findByUuid(uuid)
            request.respond(FindClanMemberByUuidResponsePacket(member))
        }
    }
}

