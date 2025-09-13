package dev.slne.surf.clan.api.common.clan.member.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import java.util.*

@Serializable
sealed class ClanMemberRemoveResult {
    protected abstract suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()
        return componentBuilder.build()
    }

    data class Success(val clan: Clan, val playerUuid: UUID) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            success("Du hast ")
            append(player.asComponent())
            success(" aus dem Clan ")
            append(clan)
            success(" entfernt.")
        }
    }

    data class CannotKickYourself(val clan: Clan) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du kannst dich nicht selbst aus dem Clan ")
            append(clan)
            error(" entfernen. Nutze stattdessen den Befehl zum Verlassen des Clans.")
        }
    }

    data class CannotKickSameOrHigherRole(val clan: Clan) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du kannst kein Mitglied mit deiner oder einer höheren Rolle aus dem Clan ")
            append(clan)
            error(" entfernen.")
        }
    }

    data class ClanNotFound(val clan: Clan) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" wurde nicht in der Datenbank gefunden.")
        }
    }

    data class OwnerCannotBeRemoved(val clan: Clan) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Besitzer des Clans ")
            append(clan)
            error(" kann nicht entfernt werden.")
        }
    }

    data class NoPermission(val clan: Clan) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du hast keine Berechtigung, um Mitglieder aus dem Clan ")
            append(clan)
            error(" zu entfernen.")
        }
    }

    data class SelfNotClanMember(val clan: Clan) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du bist kein Mitglied des Clans ")
            append(clan)
            error(" und kannst daher keine Mitglieder entfernen.")
        }
    }

    data class NotClanMember(val clan: Clan, val playerUuid: UUID) : ClanMemberRemoveResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            append(player.asComponent())
            error(" ist kein Mitglied des Clans ")
            append(clan)
            error(".")
        }
    }
}
