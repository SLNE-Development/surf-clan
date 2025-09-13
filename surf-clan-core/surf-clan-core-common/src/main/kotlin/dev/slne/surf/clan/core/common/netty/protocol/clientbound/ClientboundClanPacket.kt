package dev.slne.surf.clan.core.common.netty.protocol.clientbound

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Serializable

@Serializable
@SurfNettyPacket(id = "clan:clan_packet", flow = PacketFlow.CLIENTBOUND)
class ClientboundClanPacket(
    val clan: Clan
) : ResponseNettyPacket()