package dev.slne.surf.clan.api.common.clan.member.result.invite

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.cloud.api.common.netty.network.codec.kotlinx.java.SerializableUUID
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class ClanMemberUninviteResult() : ComponentResult {
    override val isSuccess get() = this is Success

    @Serializable
    data class Success(
        val clan: Clan,
        val playerUuid: SerializableUUID,
        val targetUuid: SerializableUUID,
    ) : ClanMemberUninviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val target = ClanPlayer[targetUuid]

            success("Du die Einladung von  ")
            append(target.asComponent())
            success(" aus dem Clan ")
            append(clan)
            success(" entfernt.")
        }
    }

    @Serializable
    data class NotInvited(
        val clan: Clan,
        val playerUuid: SerializableUUID,
        val targetUuid: SerializableUUID,
    ) : ClanMemberUninviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val target = ClanPlayer[targetUuid]

            append(target.asComponent())
            error(" hat keine Einladung zu dem Clan ")
            append(clan)
            error(".")
        }
    }
}
