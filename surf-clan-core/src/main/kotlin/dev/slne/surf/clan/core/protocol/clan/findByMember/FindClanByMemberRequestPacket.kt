package dev.slne.surf.clan.core.protocol.clan.findByMember

import dev.slne.surf.clan.core.protocol.clan.OptionalClanResponsePacket
import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class FindClanByMemberRequestPacket(
    val memberUuid: @Contextual UUID
) : RabbitRequestPacket<OptionalClanResponsePacket>()
