package dev.slne.surf.clan.api.common.clan.member.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
sealed class ClanMemberAddResult : ComponentResult {
    override val isSuccess get() = this is Success

    data class Success(
        val clan: Clan,
        val playerUuid: UUID,
        val targetUuid: UUID,
    ) : ClanMemberAddResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val target = ClanPlayer[targetUuid]

            success("Du hast ")
            append(target.asComponent())
            success(" in den Clan ")
            append(clan)
            success(" aufgenommen.")
        }
    }

    data class InviteRemoveFailed(
        val clan: Clan,
        val playerUuid: UUID,
        val targetUuid: UUID,
        val result: ClanMemberUninviteResult
    ) : ClanMemberAddResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val target = ClanPlayer[targetUuid]

            error("Die Einladung von ")
            append(target.asComponent())
            error(" in den Clan ")
            append(clan)
            error(" konnte nicht entfernt werden: ")

            append(result.asComponent())
        }
    }

    data class AlreadyMember(
        val clan: Clan,
        val playerUuid: UUID,
        val targetUuid: UUID,
    ) : ClanMemberAddResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val target = ClanPlayer[targetUuid]

            append(target.asComponent())
            error(" ist bereits Mitglied des Clans ")
            append(clan)
            error(".")
        }
    }
}
