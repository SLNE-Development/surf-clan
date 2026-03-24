package dev.slne.surf.clan.core.protocol.clan.findByID

import dev.slne.surf.clan.core.protocol.clan.OptionalClanResponsePacket
import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Serializable

@Serializable
data class FindClanByClanIDRequestPacket(
    val clanID: ULong
): RabbitRequestPacket<OptionalClanResponsePacket>()