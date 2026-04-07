package dev.slne.surf.clan.core.protocol.clan.findTagsByPrefixLimited

import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class FindClanTagsByPrefixLimitedResponsePacket(
    val tags: List<String>
): RabbitResponsePacket()
