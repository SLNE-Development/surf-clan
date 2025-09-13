package dev.slne.surf.clan.core.common.netty.protocol.serverbound

import dev.slne.surf.clan.core.common.netty.protocol.clientbound.ClientboundClanListPacket
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import kotlinx.serialization.Serializable

@Serializable
@SurfNettyPacket(id = "clan:fetch_all_clans", flow = PacketFlow.SERVERBOUND)
class ServerboundAllClansPacket() : RespondingNettyPacket<ClientboundClanListPacket>()