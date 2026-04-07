package dev.slne.surf.clan.core.protocol.invite.findPendingByInvitedAndClanName

import dev.slne.surf.rabbitmq.api.packet.RabbitRequestPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class FindPendingInviteByInvitedPlayerAndClanNameRequestPacket(
    val invited: @Contextual UUID,
    val clanName: String
) : RabbitRequestPacket<FindPendingInviteByInvitedPlayerAndClanNameResponsePacket>()