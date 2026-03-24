package dev.slne.surf.clan.core.protocol.clan.create

import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class CreateClanResponsePacket(val result: ClanCreationResult) : RabbitResponsePacket()
