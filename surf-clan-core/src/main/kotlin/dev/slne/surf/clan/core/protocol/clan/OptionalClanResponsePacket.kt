package dev.slne.surf.clan.core.protocol.clan

import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class OptionalClanResponsePacket(val clan: ClanImpl?) : RabbitResponsePacket()