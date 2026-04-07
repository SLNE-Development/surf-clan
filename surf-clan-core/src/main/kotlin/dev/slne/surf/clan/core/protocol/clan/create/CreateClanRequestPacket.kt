package dev.slne.surf.clan.core.protocol.clan.create

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import java.util.*

@Serializable
data class CreateClanRequestPacket(
    val name: String,
    val tag: String,
    val owner: @Contextual UUID,
    val tagForegroundColor: @Contextual TextColor?,
    val tagBackgroundColor: @Contextual TextColor?,
    val tagShadowColor: @Contextual ShadowColor?,
    val description: @Contextual String?,
    val discordInvite: @Contextual String?
): RabbitRequestPacket<CreateClanResponsePacket>()