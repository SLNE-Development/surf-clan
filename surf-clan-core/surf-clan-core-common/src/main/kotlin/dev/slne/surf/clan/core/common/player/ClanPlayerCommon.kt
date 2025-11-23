@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.core.common.player

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.ClanManager
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.InternalClanApi
import dev.slne.surf.cloud.api.common.player.OfflineCloudPlayer
import dev.slne.surf.cloud.api.common.player.toOfflineCloudPlayer
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.springframework.beans.factory.getBean
import java.time.ZonedDateTime
import java.util.*

@Serializable
class ClanPlayerCommon(
    override val uuid: @Contextual UUID,
    override var acceptsClanInvites: Boolean,
    override val createdAt: @Contextual ZonedDateTime,
    override val updatedAt: @Contextual ZonedDateTime
) : ClanPlayer {

    companion object {
        private val playerManager by lazy {
            InternalContextHolder.context.getBean<ClanPlayerManagerCommon>()
        }
    }

    override val offlineCloudPlayer: OfflineCloudPlayer
        get() = uuid.toOfflineCloudPlayer()

    override val clan: Clan?
        get() = ClanManager.getClanByPlayer(this)

    override suspend fun setAcceptsClanInvites(value: Boolean): Boolean {
        val result = playerManager.setAcceptsClanInvites(this, value)

        if (result) {
            acceptsClanInvites = value
            return true
        }

        return false
    }

    override suspend fun asComponent() = buildText {
        append(offlineCloudPlayer.displayName())
        primary(" TODO")
    }
}