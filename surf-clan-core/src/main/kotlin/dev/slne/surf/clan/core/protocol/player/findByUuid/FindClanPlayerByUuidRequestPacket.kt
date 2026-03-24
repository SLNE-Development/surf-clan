package dev.slne.surf.clan.core.protocol.player.findByUuid

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class FindClanPlayerByUuidRequestPacket(
    val uuid: @Contextual UUID
) : RabbitRequestPacket<FindClanPlayerByUuidResponsePacket>()