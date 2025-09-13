package dev.slne.surf.clan.core.common.netty.protocol.serverbound.options

import dev.slne.surf.clan.core.common.netty.protocol.clientbound.options.ClientboundSetDiscordInviteResultPacket
import dev.slne.surf.cloud.api.common.meta.SurfNettyPacket
import dev.slne.surf.cloud.api.common.netty.network.protocol.PacketFlow
import dev.slne.surf.cloud.api.common.netty.packet.RespondingNettyPacket
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
@SurfNettyPacket(id = "clan:set_clan_discord_invite", flow = PacketFlow.SERVERBOUND)
class ServerboundSetClanDiscordInvitePacket(
    val clanUuid: @Contextual UUID,
    val discordInvite: String?
) : RespondingNettyPacket<ClientboundSetDiscordInviteResultPacket>()