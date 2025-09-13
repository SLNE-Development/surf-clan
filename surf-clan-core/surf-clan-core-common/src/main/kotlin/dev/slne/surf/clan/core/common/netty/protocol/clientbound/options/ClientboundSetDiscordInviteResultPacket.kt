package dev.slne.surf.clan.core.common.netty.protocol.clientbound.options

import dev.slne.surf.clan.api.common.clan.result.ClanSetDiscordInviteResult
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.ResponseNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:set_clan_discord_invite_result", flow = PacketFlow.CLIENTBOUND)
class ClientboundSetDiscordInviteResultPacket(
    val setByUuid: @Contextual UUID,
    val result: ClanSetDiscordInviteResult
) : ResponseNettyPacket()