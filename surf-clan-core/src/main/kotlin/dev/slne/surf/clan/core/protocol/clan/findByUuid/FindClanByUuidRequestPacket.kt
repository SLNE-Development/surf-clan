package dev.slne.surf.clan.core.protocol.clan.findByUuid

import dev.slne.surf.clan.core.protocol.clan.OptionalClanResponsePacket
import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class FindClanByUuidRequestPacket(
    val clanUuid: @Contextual UUID
) : RabbitRequestPacket<OptionalClanResponsePacket>()