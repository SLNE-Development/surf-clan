package dev.slne.surf.clan.core.protocol.invite.accept

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class AcceptClanInviteRequestPacket(
    val inviteID: ULong,
    val invitee: @Contextual UUID,
    val invitedBy: @Contextual UUID
) : RabbitRequestPacket<PrimitiveResponse.BooleanResponsePacket>() {
}