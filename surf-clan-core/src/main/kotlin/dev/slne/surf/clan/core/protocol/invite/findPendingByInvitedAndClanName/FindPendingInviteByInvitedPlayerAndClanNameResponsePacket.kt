package dev.slne.surf.clan.core.protocol.invite.findPendingByInvitedAndClanName

import dev.slne.surf.clan.core.invite.ClanInviteImpl
import dev.slne.surf.rabbitmq.api.packet.RabbitResponsePacket
import kotlinx.serialization.Serializable

@Serializable
data class FindPendingInviteByInvitedPlayerAndClanNameResponsePacket(
    val invite: ClanInviteImpl?
) : RabbitResponsePacket()