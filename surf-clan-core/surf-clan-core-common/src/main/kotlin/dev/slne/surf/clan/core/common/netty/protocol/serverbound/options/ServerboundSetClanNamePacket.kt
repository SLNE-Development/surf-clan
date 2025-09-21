package dev.slne.surf.clan.core.common.netty.protocol.serverbound.options

import dev.slne.surf.clan.core.common.netty.protocol.clientbound.options.ClientboundSetClanNameResultPacket
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:set_clan_name", flow = PacketFlow.SERVERBOUND)
class ServerboundSetClanNamePacket(
    val clanUuid: @Contextual UUID,
    val playerUuid: @Contextual UUID,
    val name: String,
) : RespondingNettyPacket<ClientboundSetClanNameResultPacket>()