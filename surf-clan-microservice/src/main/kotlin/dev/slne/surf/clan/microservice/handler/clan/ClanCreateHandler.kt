package dev.slne.surf.clan.microservice.handler.clan

import dev.slne.surf.clan.core.protocol.clan.create.CreateClanRequestPacket
import dev.slne.surf.clan.core.protocol.clan.create.CreateClanResponsePacket
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import dev.slne.surf.rabbitmq.api.handler.RabbitHandler
import kotlinx.coroutines.launch

object ClanCreateHandler {

    @RabbitHandler
    fun handleCreateClan(request: CreateClanRequestPacket) {
        val (name, tag, owner, tagForegroundColor, tagBackgroundColor, tagShadowColor, description, discordInvite) = request

        request.launch {
            val result = ClanRepository.Companion.create(
                name = name,
                tag = tag,
                owner = owner,
                tagForegroundColor = tagForegroundColor,
                tagBackgroundColor = tagBackgroundColor,
                tagShadowColor = tagShadowColor,
                description = description,
                discordInvite = discordInvite
            )

            request.respond(CreateClanResponsePacket(result))
        }
    }
}