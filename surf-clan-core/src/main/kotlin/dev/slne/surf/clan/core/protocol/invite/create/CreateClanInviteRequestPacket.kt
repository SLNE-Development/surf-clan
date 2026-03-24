package dev.slne.surf.clan.core.protocol.invite.create

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CreateClanInviteRequestPacket(
    val clanID: ULong,
    val invitee: @Contextual UUID,
    val invitedBy: @Contextual UUID
) : RabbitRequestPacket<CreateClanInviteResponsePacket>()