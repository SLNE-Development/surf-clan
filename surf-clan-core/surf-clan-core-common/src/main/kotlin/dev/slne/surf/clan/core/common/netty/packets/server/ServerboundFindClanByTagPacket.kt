package dev.slne.surf.clan.core.common.netty.packets.server

import dev.slne.surf.clan.core.common.netty.packets.bidirectional.BiDirectionalGenericClanResponsePacket
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import kotlinx.serialization.Serializable

@Serializable
@SurfNettyPacket("surf:clan:find_clan_by_tag", PacketFlow.SERVERBOUND)
class ServerboundFindClanByTagPacket(val tag: String) :
    RespondingNettyPacket<BiDirectionalGenericClanResponsePacket>()