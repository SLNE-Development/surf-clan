package dev.slne.surf.clan.core.common.netty.protocol.clientbound.options

import dev.slne.surf.clan.api.common.clan.result.ClanSetNameResult
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:set_clan_name_result", flow = PacketFlow.CLIENTBOUND)
class ClientboundSetClanNameResultPacket(
    val playerUuid: @Contextual UUID,
    val result: ClanSetNameResult
) : ResponseNettyPacket()