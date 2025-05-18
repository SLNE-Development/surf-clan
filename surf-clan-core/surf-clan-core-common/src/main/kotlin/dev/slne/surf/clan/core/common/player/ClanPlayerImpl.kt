package dev.slne.surf.clan.core.common.player

import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.cloud.api.common.netty.network.codec.kotlinx.java.SerializableUUID
import kotlinx.serialization.Serializable

@Serializable
data class ClanPlayerImpl(
    override val uuid: SerializableUUID,
    override var acceptsClanInvites: Boolean
) : ClanPlayer