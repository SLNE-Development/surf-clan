package dev.slne.surf.clan.core.common.netty.protocol.serverbound.options

import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.core.common.netty.protocol.clientbound.options.ClientboundSetClanTagResultPacket
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:set_clan_tag", flow = PacketFlow.SERVERBOUND)
class ServerboundSetClanTagPacket(
    val clanUuid: @Contextual UUID,
    val clanTag: ClanTag,
    val setByUuid: @Contextual UUID
) : RespondingNettyPacket<ClientboundSetClanTagResultPacket>()