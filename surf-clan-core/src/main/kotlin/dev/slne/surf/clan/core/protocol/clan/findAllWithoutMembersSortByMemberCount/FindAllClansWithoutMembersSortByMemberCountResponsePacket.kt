package dev.slne.surf.clan.core.protocol.clan.findAllWithoutMembersSortByMemberCount

import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class FindAllClansWithoutMembersSortByMemberCountResponsePacket(val result: Collection<ClanImpl>) :
    RabbitResponsePacket()