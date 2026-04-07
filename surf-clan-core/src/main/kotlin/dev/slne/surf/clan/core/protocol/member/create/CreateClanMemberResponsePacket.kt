package dev.slne.surf.clan.core.protocol.member.create

import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class CreateClanMemberResponsePacket(val result: ClanMemberAddResult) : RabbitResponsePacket()