package dev.slne.surf.clan.core.protocol.invite.findPendingByInvited

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class FindPendingInvitesByInvitedRequestPacket(
    val invited: @Contextual UUID
) : RabbitRequestPacket<FindPendingInvitesByInvitedResponsePacket>()