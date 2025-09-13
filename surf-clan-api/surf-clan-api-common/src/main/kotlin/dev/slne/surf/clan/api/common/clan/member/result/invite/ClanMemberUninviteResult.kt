package dev.slne.surf.clan.api.common.clan.member.result.invite

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import java.util.*

@Serializable
sealed class ClanMemberUninviteResult() {
    protected abstract suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()

        return componentBuilder.build()
    }

    val isSuccess get() = this is Success

    data class Success(val clan: Clan, val playerUuid: UUID) : ClanMemberUninviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            success("Du die Einladung von  ")
            append(player.asComponent())
            success(" aus dem Clan ")
            append(clan)
            success(" entfernt.")
        }
    }

    data class ClanNotFound(val clan: Clan) : ClanMemberUninviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" wurde nicht in der Datenbank gefunden.")
        }
    }

    data class NoPermissions(val clan: Clan) : ClanMemberUninviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du hast keine Berechtigung, um Einladungen im Clan ")
            append(clan)
            error(" zu entfernen.")
        }
    }

    data class NotClanMember(val clan: Clan) : ClanMemberUninviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du bist kein Mitglied des Clans ")
            append(clan)
            error(" und kannst daher keine Einladungen entfernen.")
        }
    }

    data class NotInvited(val clan: Clan, val playerUuid: UUID) : ClanMemberUninviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            append(player.asComponent())
            error(" hat keine Einladung zu dem Clan ")
            append(clan)
            error(".")
        }
    }
}
