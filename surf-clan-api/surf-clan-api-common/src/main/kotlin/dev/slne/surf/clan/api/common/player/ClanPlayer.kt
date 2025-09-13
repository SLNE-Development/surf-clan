package dev.slne.surf.clan.api.common.player

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.cloud.api.common.player.OfflineCloudPlayer
import net.kyori.adventure.text.Component
import java.time.ZonedDateTime
import java.util.*

interface ClanPlayer {

    val uuid: UUID
    val offlineCloudPlayer: OfflineCloudPlayer

    val acceptsClanInvites: Boolean
    suspend fun setAcceptsClanInvites(value: Boolean): Boolean

    val clan: Clan?

    val createdAt: ZonedDateTime
    val updatedAt: ZonedDateTime

    suspend fun asComponent(): Component

    companion object {
        suspend operator fun get(uuid: UUID): ClanPlayer = ClanPlayerManager[uuid]
    }
}

suspend fun OfflineCloudPlayer.clanPlayer() = ClanPlayer[this.uuid]