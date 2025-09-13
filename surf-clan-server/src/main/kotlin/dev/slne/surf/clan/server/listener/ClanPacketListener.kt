package dev.slne.surf.clan.server.listener

import dev.slne.surf.clan.core.common.netty.protocol.clientbound.ClientboundClanListPacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.ServerboundAllClansPacket
import dev.slne.surf.clan.server.clan.ClanManagerServer
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacketHandler
import org.springframework.stereotype.Component

@Component
class ClanPacketListener(
    private val clanManagerServer: ClanManagerServer
) {
    @SurfNettyPacketHandler
    suspend fun handleAllClansPacket(packet: ServerboundAllClansPacket) {
        packet.respond(ClientboundClanListPacket(clanManagerServer.clans))
    }
}