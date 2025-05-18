package dev.slne.surf.clan.core.common.netty.packets.bidirectional

import dev.slne.surf.clan.core.common.ClanImpl
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Serializable

@Serializable
@SurfNettyPacket("surf:clan:generic_clan_response", PacketFlow.BIDIRECTIONAL)
class BiDirectionalGenericClanResponsePacket(val clan: ClanImpl?) : ResponseNettyPacket()