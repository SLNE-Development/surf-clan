package dev.slne.surf.clan.core.protocol.member.changeRole

import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import dev.slne.surf.rabbitmq.api.packet.standard.response.primitive.PrimitiveResponse
import kotlinx.serialization.Serializable

@Serializable
data class ChangeClanMemberRoleRequestPacket(
    val memberID: ULong,
    val role: ClanMemberRole
) : RabbitRequestPacket<PrimitiveResponse.BooleanResponsePacket>()