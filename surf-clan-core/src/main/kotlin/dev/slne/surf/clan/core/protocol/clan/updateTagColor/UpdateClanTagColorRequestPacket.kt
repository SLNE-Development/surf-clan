package dev.slne.surf.clan.core.protocol.clan.updateTagColor

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor

@Serializable
data class UpdateClanTagColorRequestPacket(
    val clanID: ULong,
    val tagForegroundColor: @Contextual TextColor?,
    val tagBackgroundColor: @Contextual TextColor?,
    val tagShadowColor: @Contextual ShadowColor?
) : RabbitRequestPacket<PrimitiveResponse.BooleanResponsePacket>()
