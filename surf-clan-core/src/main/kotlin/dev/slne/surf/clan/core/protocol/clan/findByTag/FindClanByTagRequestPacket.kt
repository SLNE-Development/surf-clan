package dev.slne.surf.clan.core.protocol.clan.findByTag

import dev.slne.surf.clan.core.protocol.clan.OptionalClanResponsePacket
import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Serializable

@Serializable
data class FindClanByTagRequestPacket(val tag: String) : RabbitRequestPacket<OptionalClanResponsePacket>()