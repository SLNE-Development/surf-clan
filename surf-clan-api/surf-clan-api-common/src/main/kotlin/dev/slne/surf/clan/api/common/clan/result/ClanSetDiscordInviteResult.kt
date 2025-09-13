package dev.slne.surf.clan.api.common.clan.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
abstract class ClanSetDiscordInviteResult {
    protected abstract suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()
        return componentBuilder.build()
    }

    data class Success(val clan: Clan, val discordInvite: String?) : ClanSetDiscordInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            if (discordInvite != null) {
                success("Du hast den Discord-Einladungslink des Clans ")
                append(clan)
                success(" auf ")
                variableValue(discordInvite)
                success(" gesetzt.")
            } else {
                success("Du hast den Discord-Einladungslink des Clans entfernt.")
            }
        }
    }

    data class ClanNotFound(val clan: Clan) : ClanSetDiscordInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" wurde nicht in der Datenbank gefunden.")
        }
    }

    data class InvalidDiscordInvite(val discordInvite: String) : ClanSetDiscordInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Discord-Einladungslink ")
            variableValue(discordInvite)
            error(" ist ungültig.")
        }
    }

    data class NotClanMember(val clan: Clan) : ClanSetDiscordInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du bist kein Mitglied des Clans ")
            append(clan)
            error(" und kannst den Discord-Einladungslink daher nicht ändern.")
        }
    }

    data class NoPermission(val clan: Clan) : ClanSetDiscordInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du hast keine Berechtigung, um den Discord-Einladungslink des Clans ")
            append(clan)
            error(" zu ändern.")
        }
    }

}