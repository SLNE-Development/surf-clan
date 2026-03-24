package dev.slne.surf.clan.core.protocol.player.updateAccepsClanInvites

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClanPlayerAcceptsInvitesRequestPacket(
    val playerID: ULong,
    val acceptsClanInvites: Boolean
) : RabbitRequestPacket<PrimitiveResponse.BooleanResponsePacket>()