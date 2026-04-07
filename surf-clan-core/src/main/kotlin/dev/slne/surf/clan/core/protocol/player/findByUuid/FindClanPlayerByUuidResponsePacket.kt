package dev.slne.surf.clan.core.protocol.player.findByUuid

import dev.slne.surf.clan.core.player.ClanPlayerImpl
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class FindClanPlayerByUuidResponsePacket(
    val player: ClanPlayerImpl
): RabbitResponsePacket() {
}