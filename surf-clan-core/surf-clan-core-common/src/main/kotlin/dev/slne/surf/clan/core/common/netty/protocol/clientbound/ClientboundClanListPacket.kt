package dev.slne.surf.clan.core.common.netty.protocol.clientbound

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import it.unimi.dsi.fastutil.objects.ObjectSet
import kotlinx.serialization.Serializable

@Serializable
@SurfNettyPacket(id = "clan:clan_list_packet", flow = PacketFlow.CLIENTBOUND)
class ClientboundClanListPacket(
    val clans: ObjectSet<Clan>
) : ResponseNettyPacket()