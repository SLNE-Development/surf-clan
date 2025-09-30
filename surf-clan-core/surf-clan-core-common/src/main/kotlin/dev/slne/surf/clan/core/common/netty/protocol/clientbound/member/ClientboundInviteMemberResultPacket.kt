package dev.slne.surf.clan.core.common.netty.protocol.clientbound.member

import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberInviteResult
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:member_invite_result", flow = PacketFlow.CLIENTBOUND)
class ClientboundInviteMemberResultPacket(
    val clanUuid: @Contextual UUID,
    val playerUuid: @Contextual UUID,
    val targetUuid: @Contextual UUID,
    val result: ClanMemberInviteResult
) : ResponseNettyPacket()