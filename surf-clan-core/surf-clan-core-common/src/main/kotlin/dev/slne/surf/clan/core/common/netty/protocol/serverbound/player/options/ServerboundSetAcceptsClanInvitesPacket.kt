package dev.slne.surf.clan.core.common.netty.protocol.serverbound.player.options

import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.network.protocol.boolean.BooleanResponsePacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:player_set_accepts_clan_requests", flow = PacketFlow.SERVERBOUND)
class ServerboundSetAcceptsClanInvitesPacket(
    val playerUuid: @Contextual UUID,
    val newValue: Boolean
) : BooleanResponsePacket()