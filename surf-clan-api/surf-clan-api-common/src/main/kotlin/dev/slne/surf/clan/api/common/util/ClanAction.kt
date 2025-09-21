@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.api.common.util

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer


interface ClanAction<T> {
    val permission: ClanPermission

    data class EmptyArguments(val nothing: Boolean = true)

    open class RequiredTargetActionArguments(
        val target: ClanPlayer,
        val targetMember: ClanMember
    )

    suspend fun authorize(
        clan: Clan,
        player: ClanPlayer,
        arguments: T
    ): ComponentResult {
        val member = clan.getMember(player)
            ?: return ComponentResult.SelfNotClanMember(clan, player.uuid)

        if (!member.hasPermission(permission)) {
            return ComponentResult.NoPermissions(clan, player.uuid, permission)
        }

        return ComponentResult.EmptySuccess
    }

    suspend fun action(
        clan: Clan,
        player: ClanPlayer,
        arguments: T
    ): ComponentResult

    suspend fun execute(
        clan: Clan,
        player: ClanPlayer,
        arguments: T
    ): ComponentResult {
        val authorization = authorize(clan, player, arguments)

        if (authorization.isError) {
            return authorization
        }

        return action(clan, player, arguments)
    }

}