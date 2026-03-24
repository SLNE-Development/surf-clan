package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.findTagsByPrefixLimited.FindClanTagsByPrefixLimitedRequestPacket
import dev.slne.surf.clan.core.protocol.clan.findTagsByPrefixLimited.FindClanTagsByPrefixLimitedResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object FindClanTagsByPrefixLimitedHandler {

    @RabbitHandler
    fun handleFindClanTagsByPrefixLimited(request: FindClanTagsByPrefixLimitedRequestPacket) {
        request.launch {
            val clanTags = ClanRepository.suggestTagsByPrefix(request.prefix, request.limit)
            request.respond(FindClanTagsByPrefixLimitedResponsePacket(clanTags))
        }
    }
}