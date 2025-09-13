package dev.slne.surf.clan.core.common.netty.protocol.clientbound.player

import dev.slne.surf.clan.core.common.player.ClanPlayerCommon
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Serializable

@Serializable
@SurfNettyPacket(id = "clan:clan_player", flow = PacketFlow.CLIENTBOUND)
class ClientboundClanPlayerPacket(
    val clanPlayer: ClanPlayerCommon
) : ResponseNettyPacket()