package dev.slne.surf.clan.core.protocol.clan.findTagsByPrefixLimited

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Serializable

@Serializable
data class FindClanTagsByPrefixLimitedRequestPacket(
    val prefix: String,
    val limit: Int
) : RabbitRequestPacket<FindClanTagsByPrefixLimitedResponsePacket>()