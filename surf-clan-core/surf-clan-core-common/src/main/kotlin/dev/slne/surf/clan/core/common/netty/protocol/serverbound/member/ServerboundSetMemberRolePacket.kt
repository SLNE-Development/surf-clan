package dev.slne.surf.clan.core.common.netty.protocol.serverbound.member

import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.core.common.netty.protocol.clientbound.member.ClientboundSetMemberRoleResultPacket
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:member_set_role", flow = PacketFlow.SERVERBOUND)
class ServerboundSetMemberRolePacket(
    val clanUuid: @Contextual UUID,
    val memberUuid: @Contextual UUID,
    val setByUuid: @Contextual UUID,
    val role: ClanMemberRole,
) : RespondingNettyPacket<ClientboundSetMemberRoleResultPacket>()