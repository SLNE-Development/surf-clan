package dev.slne.surf.clan.core.common.netty.protocol.clientbound.member

import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberAddResult
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:member_add_result", flow = PacketFlow.CLIENTBOUND)
class ClientboundAddMemberResultPacket(
    val clanUuid: @Contextual UUID,
    val playerUuid: @Contextual UUID,
    val addedByUuid: @Contextual UUID,
    val role: ClanMemberRole,
    val result: ClanMemberAddResult
) : ResponseNettyPacket()