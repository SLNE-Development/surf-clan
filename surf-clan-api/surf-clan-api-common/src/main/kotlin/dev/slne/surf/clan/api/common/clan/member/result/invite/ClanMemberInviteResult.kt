package dev.slne.surf.clan.api.common.clan.member.result.invite

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class ClanMemberInviteResult : ComponentResult {
    override val isSuccess get() = this is Success

    @Serializable
    data class Success(
        val clan: Clan,
        val playerUuid: @Contextual UUID,
        val targetUuid: @Contextual UUID
    ) : ClanMemberInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val target = ClanPlayer[targetUuid]

            success("Du hast ")
            append(target.asComponent())
            success(" in den Clan ")
            append(clan)
            success(" eingeladen.")
        }
    }

    @Serializable
    data class AlreadyInvited(
        val clan: Clan,
        val playerUuid: @Contextual UUID,
        val targetUuid: @Contextual UUID
    ) : ClanMemberInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val target = ClanPlayer[targetUuid]

            append(target.asComponent())
            error(" wurde bereits in den Clan ")
            append(clan)
            error(" eingeladen.")
        }
    }
}
