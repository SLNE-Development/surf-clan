package dev.slne.surf.clan.core.common.netty.packets.server

import dev.slne.surf.clan.core.common.netty.packets.client.ClientboundCreateClanResponsePacket
import dev.slne.surf.clan.core.common.player.ClanPlayerImpl
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import kotlinx.serialization.Serializable

@Serializable
@SurfNettyPacket("surf:clan:create", PacketFlow.SERVERBOUND)
class ServerboundCreateClanPacket(
    val name: String,
    val tag: String,
    val createdBy: ClanPlayerImpl
) : RespondingNettyPacket<ClientboundCreateClanResponsePacket>()