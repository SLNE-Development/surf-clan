package dev.slne.surf.clan.core.protocol.clan.findAllWithoutMembersSortByMemberCount

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Serializable

@Serializable
class FindAllClansWithoutMembersSortByMemberCountRequestPacket :
    RabbitRequestPacket<FindAllClansWithoutMembersSortByMemberCountResponsePacket>()