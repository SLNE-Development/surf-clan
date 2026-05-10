package dev.slne.surf.clan.core.protocol.clan.updateNameAndTag

import dev.slne.clan.api.clan.update.ClanNameAndTag
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClanNameAndTagResponsePacket(
    val result: ClanNameAndTag.UpdateResult
) : RabbitResponsePacket()