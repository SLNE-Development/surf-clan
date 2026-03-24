package dev.slne.surf.clan.core.protocol.clan.delete

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.serialization.Serializable

@Serializable
data class DeleteClanRequestPacket(
    val clanID: ULong
) : RabbitRequestPacket<PrimitiveResponse.BooleanResponsePacket>()
