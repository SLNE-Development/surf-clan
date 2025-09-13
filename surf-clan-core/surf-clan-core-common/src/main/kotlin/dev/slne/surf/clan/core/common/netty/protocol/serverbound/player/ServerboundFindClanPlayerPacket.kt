package dev.slne.surf.clan.core.common.netty.protocol.serverbound.player

import dev.slne.surf.clan.core.common.netty.protocol.clientbound.player.ClientboundClanPlayerPacket
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:player_find_by_uuid", flow = PacketFlow.SERVERBOUND)
class ServerboundFindClanPlayerPacket(
    val playerUuid: @Contextual UUID
) : RespondingNettyPacket<ClientboundClanPlayerPacket>()