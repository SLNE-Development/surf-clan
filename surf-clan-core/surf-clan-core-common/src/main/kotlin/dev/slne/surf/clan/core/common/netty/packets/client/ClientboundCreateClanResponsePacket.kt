package dev.slne.surf.clan.core.common.netty.packets.client

import dev.slne.surf.clan.api.common.results.ClanCreateResult
import dev.slne.surf.clan.core.common.ClanImpl
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket

@SurfNettyPacket("surf:clan:create_clan_response", PacketFlow.CLIENTBOUND)
class ClientboundCreateClanResponsePacket(
    val response: Pair<ClanCreateResult, ClanImpl?>
) : ResponseNettyPacket()