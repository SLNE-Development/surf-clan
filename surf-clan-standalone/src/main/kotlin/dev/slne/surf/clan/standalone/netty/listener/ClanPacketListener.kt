package dev.slne.surf.clan.standalone.netty.listener

import dev.slne.surf.clan.core.common.netty.packets.bidirectional.BiDirectionalGenericClanResponsePacket
import dev.slne.surf.clan.core.common.netty.packets.client.ClientboundCreateClanResponsePacket
import dev.slne.surf.clan.core.common.netty.packets.server.ServerboundCreateClanPacket
import dev.slne.surf.clan.core.common.netty.packets.server.ServerboundFindClanByNamePacket
import dev.slne.surf.clan.core.common.netty.packets.server.ServerboundFindClanByPlayerPacket
import dev.slne.surf.clan.core.common.netty.packets.server.ServerboundFindClanByTagPacket
import dev.slne.surf.clan.standalone.ClanService
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacketHandler
import org.springframework.stereotype.Component

@Component
class ClanPacketListener(
    private val clanService: ClanService,
) {

    @SurfNettyPacketHandler
    suspend fun onFindClanByName(packet: ServerboundFindClanByNamePacket) {
        packet.respond(BiDirectionalGenericClanResponsePacket(clanService.findClanByName(packet.name)))
    }

    @SurfNettyPacketHandler
    suspend fun onFindClanByTag(packet: ServerboundFindClanByTagPacket) {
        packet.respond(BiDirectionalGenericClanResponsePacket(clanService.findClanByTag(packet.tag)))
    }

    suspend fun onFindClanByPlayer(packet: ServerboundFindClanByPlayerPacket) {
        packet.respond(BiDirectionalGenericClanResponsePacket(clanService.findClanByPlayer(packet.player)))
    }

    @SurfNettyPacketHandler
    suspend fun onClanCreate(packet: ServerboundCreateClanPacket) {
        packet.respond(ClientboundCreateClanResponsePacket(clanService.createClan(packet)))
    }
}