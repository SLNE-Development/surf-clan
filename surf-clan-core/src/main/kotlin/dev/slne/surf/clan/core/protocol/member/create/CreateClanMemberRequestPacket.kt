package dev.slne.surf.clan.core.protocol.member.create

import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class CreateClanMemberRequestPacket(
    val clanID: ULong,
    val player: @Contextual UUID,
    val role: ClanMemberRole,
    val invitedBy: @Contextual UUID?
) : RabbitRequestPacket<CreateClanMemberResponsePacket>()