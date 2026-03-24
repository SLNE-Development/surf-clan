package dev.slne.surf.clan.core.protocol.invite.create

import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class CreateClanInviteResponsePacket(val result: ClanInviteResult) : RabbitResponsePacket()