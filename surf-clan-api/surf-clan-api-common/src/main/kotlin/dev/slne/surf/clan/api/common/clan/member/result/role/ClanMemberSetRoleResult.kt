package dev.slne.surf.clan.api.common.clan.member.result.role

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import kotlinx.serialization.Serializable

@Serializable
sealed class ClanMemberSetRoleResult : ComponentResult {
    override val isSuccess get() = this is Success

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

    data class CannotChangeOwnRole(val clan: Clan) : ClanMemberSetRoleResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du kannst deine eigene Rolle nicht ändern.")
        }
    }

    data class OtherSameOrHigherRole(
        val clan: Clan,
        val self: ClanMember,
        val other: ClanMember,
        val operation: Boolean
    ) : ClanMemberSetRoleResult() {
        override suspend fun SurfComponentBuilder.buildMessage() {
            error("Du kannst ")
            append(other.clanPlayer().asComponent())
            error(" nicht ")

            if (operation) {
                error("heraufstufen")
            } else {
                error("herabstufen")
            }

            error(", da du entweder die selbe oder eine niedrigere Rolle hast.")
        }
    }
}