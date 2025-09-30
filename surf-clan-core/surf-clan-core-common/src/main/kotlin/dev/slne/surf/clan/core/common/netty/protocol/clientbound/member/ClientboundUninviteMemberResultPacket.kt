package dev.slne.surf.clan.core.common.netty.protocol.clientbound.member

import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:member_uninvite_result", flow = PacketFlow.CLIENTBOUND)
class ClientboundUninviteMemberResultPacket(
    val clanUuid: @Contextual UUID,
    val playerUuid: @Contextual UUID,
    val targetUuid: @Contextual UUID,
    val result: ClanMemberUninviteResult
) : ResponseNettyPacket()