package dev.slne.surf.clan.core.protocol.clan.updateNameAndTag

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Serializable

@Serializable
data class UpdateClanNameAndTagRequestPacket(
    val clanID: ULong,
    val name: String?,
    val tag: String?
) : RabbitRequestPacket<UpdateClanNameAndTagResponsePacket>()