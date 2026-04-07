package dev.slne.surf.clan.microservice.handler.member

import dev.slne.surf.clan.core.protocol.member.create.CreateClanMemberRequestPacket
import dev.slne.surf.clan.core.protocol.member.create.CreateClanMemberResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanMemberRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object CreateClanMemberHandler {

    @RabbitHandler
    fun handleCreateClanMember(request: CreateClanMemberRequestPacket) {
        val (clanID, player, role, invitedBy) = request

        request.launch {
            val result = ClanMemberRepository.createMember(
                clanID = clanID,
                player = player,
                role = role,
                invitedBy = invitedBy
            )

            request.respond(CreateClanMemberResponsePacket(result))
        }
    }
}

