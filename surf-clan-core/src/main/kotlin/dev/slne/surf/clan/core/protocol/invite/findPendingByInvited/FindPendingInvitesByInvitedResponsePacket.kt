package dev.slne.surf.clan.core.protocol.invite.findPendingByInvited

import dev.slne.surf.clan.core.invite.ClanInviteImpl
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class FindPendingInvitesByInvitedResponsePacket(val invites: List<ClanInviteImpl>) : RabbitResponsePacket()