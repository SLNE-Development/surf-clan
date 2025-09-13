package dev.slne.surf.clan.core.common.netty.protocol.clientbound.member

import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberRemoveResult
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:member_remove_result", flow = PacketFlow.CLIENTBOUND)
class ClientboundRemoveMemberResultPacket(
    val clanUuid: @Contextual UUID,
    val playerUuid: @Contextual UUID,
    val removedByUuid: @Contextual UUID,
    val result: ClanMemberRemoveResult
) : ResponseNettyPacket()