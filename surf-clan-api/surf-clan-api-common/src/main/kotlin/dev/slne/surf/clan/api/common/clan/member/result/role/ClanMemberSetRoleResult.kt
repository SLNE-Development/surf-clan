package dev.slne.surf.clan.api.common.clan.member.result.role

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component

@Serializable
sealed class ClanMemberSetRoleResult {
    protected abstract suspend fun SurfComponentBuilder.buildMessage()

    suspend fun asComponent(): Component {
        val componentBuilder = SurfComponentBuilder.builder()
        componentBuilder.buildMessage()
        return componentBuilder.build()
    }

    data class Success(
        val clan: Clan,
        val member: ClanMember,
        val oldRole: ClanMemberRole,
        val newRole: ClanMemberRole
    ) : ClanMemberSetRoleResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            success("Die Rolle von ")
            append(member.asComponent())
            success(" im Clan ")
            append(clan)
            success(" wurde von ")
            append(oldRole.displayName)
            success(" zu ")
            append(newRole.displayName)
            success(" geändert.")
        }
    }

    data class NoPermissions(val clan: Clan) : ClanMemberSetRoleResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du hast keine Berechtigung, um die Rolle von Mitgliedern im Clan ")
            append(clan)
            error(" zu ändern.")
        }
    }

    data class ClanNotFound(val clan: Clan) : ClanMemberSetRoleResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Clan ")
            append(clan)
            error(" konnte nicht gefunden werden.")
        }
    }

    data class PlayerNotFound(val player: ClanPlayer) : ClanMemberSetRoleResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Der Spieler ")
            append(player.asComponent())
            error(" konnte nicht gefunden werden.")
        }
    }

    data class MemberNotFound(val clan: Clan, val member: ClanMember) : ClanMemberSetRoleResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Das Mitglied ")
            append(member.asComponent())
            error(" ist kein Mitglied des Clans ")
            append(clan)
            error(".")
        }
    }
}