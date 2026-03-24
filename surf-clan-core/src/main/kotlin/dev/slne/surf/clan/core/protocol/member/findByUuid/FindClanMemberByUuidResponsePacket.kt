package dev.slne.surf.clan.core.protocol.member.findByUuid

import dev.slne.surf.clan.core.member.ClanMemberImpl
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class FindClanMemberByUuidResponsePacket(
    val member: ClanMemberImpl?
) : RabbitResponsePacket()