package dev.slne.surf.clan.core.protocol.clan.updateDescription

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClanDescriptionRequestPacket(
    val clanID: ULong,
    val description: String?
): RabbitRequestPacket<PrimitiveResponse.BooleanResponsePacket>()
