package dev.slne.surf.clan.api.common.clan.member.result.invite

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import java.util.*

@Serializable
sealed class ClanMemberInviteResult {
    protected abstract suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()
        return componentBuilder.build()
    }

    data class Success(val clan: Clan, val playerUuid: UUID) : ClanMemberInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            success("Du hast ")
            append(player.asComponent())
            success(" in den Clan ")
            append(clan)
            success(" eingeladen.")
        }
    }

    data class ClanNotFound(val clan: Clan) : ClanMemberInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" wurde nicht in der Datenbank gefunden.")
        }
    }

    data class NotClanMember(val clan: Clan) : ClanMemberInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du bist kein Mitglied des Clans ")
            append(clan)
            error(" und kannst daher keine Mitglieder einladen.")
        }
    }

    data class NoPermissions(val clan: Clan) : ClanMemberInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du hast keine Berechtigung, um Mitglieder in den Clan ")
            append(clan)
            error(" einzuladen.")
        }
    }

    data class AlreadyMember(val clan: Clan, val playerUuid: UUID) : ClanMemberInviteResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            val player = ClanPlayer[playerUuid]

            append(player.asComponent())
            error(" ist bereits Mitglied des Clans ")
            append(clan)
            error(".")
        }
    }
}
