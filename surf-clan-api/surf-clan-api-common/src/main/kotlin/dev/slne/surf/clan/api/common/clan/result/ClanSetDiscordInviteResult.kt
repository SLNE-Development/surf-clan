package dev.slne.surf.clan.api.common.clan.result

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
abstract class ClanSetDiscordInviteResult : ComponentResult {
    override val isSuccess get() = this is Success

    data class Success(
        val clan: Clan,
        val playerUuid: UUID,
        val discordInvite: String?
    ) : ClanSetDiscordInviteResult() {
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

    data class InvalidDiscordInvite(val discordInvite: String) : ClanSetDiscordInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Discord-Einladungslink ")
            variableValue(discordInvite)
            error(" ist ungültig.")
        }
    }

}