package dev.slne.surf.clan.api.common.clan.member.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import java.util.*

@Serializable
sealed class ClanMemberAddResult {
    protected abstract suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()
        return componentBuilder.build()
    }

    data class Success(val clan: Clan, val playerUuid: UUID) : ClanMemberAddResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            success("Du hast ")
            append(player.asComponent())
            success(" in den Clan ")
            append(clan)
            success(" aufgenommen.")
        }
    }

    data class ClanNotFound(val clan: Clan) : ClanMemberAddResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" wurde nicht in der Datenbank gefunden.")
        }
    }

    data class InviteRemoveFailed(
        val clan: Clan,
        val playerUuid: UUID,
        val result: ClanMemberUninviteResult
    ) : ClanMemberAddResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            error("Die Einladung von ")
            append(player.asComponent())
            error(" in den Clan ")
            append(clan)
            error(" konnte nicht entfernt werden: ")

            append(result.asComponent())
        }
    }

    data class AlreadyMember(val clan: Clan, val playerUuid: UUID) : ClanMemberAddResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            append(player.asComponent())
            error(" ist bereits Mitglied des Clans ")
            append(clan)
            error(".")
        }
    }
}
