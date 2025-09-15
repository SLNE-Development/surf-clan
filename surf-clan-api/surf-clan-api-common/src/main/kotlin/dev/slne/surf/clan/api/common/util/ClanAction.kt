package dev.slne.surf.clan.api.common.util

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.objectListOf
import dev.slne.surf.surfapi.core.api.util.toObjectList
import it.unimi.dsi.fastutil.objects.ObjectList
import kotlin.reflect.KProperty

class ClanActionArgument(
    val key: String,
    val value: Any?,
)

class ClanActionExtraArguments(args: ObjectList<ClanActionArgument>) {
    private val _args = args
    val args = _args.freeze()

    constructor(vararg args: Pair<String, Any?>) : this(args.map {
        ClanActionArgument(
            it.first,
            it.second
        )
    }.toObjectList())

    operator fun plus(argument: ClanActionArgument) = _args.add(argument)

    operator fun <P> getValue(
        thisRef: Any?,
        property: KProperty<*>
    ) = args.firstOrNull { it.key == property.name }?.value as P

    companion object {
        val EMPTY = ClanActionExtraArguments(objectListOf())
    }
}

interface ClanAction {
    val permission: ClanPermission

    @Suppress("UNCHECKED_CAST")
    suspend fun authorizeExtra(
        clan: Clan,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ): ComponentResult? = null

    suspend fun authorize(
        clan: Clan,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ): ComponentResult {
        val member = clan.getMember(player)
            ?: return ComponentResult.SelfNotClanMember(clan, player.uuid)

        if (!member.hasPermission(permission)) {
            return ComponentResult.NoPermissions(clan, player.uuid, permission)
        }

        arguments + ClanActionArgument("selfMember", member)

        val extra = authorizeExtra(clan, player, arguments)

        if (extra != null && extra.isError) {
            return extra
        }

        return ComponentResult.EmptySuccess
    }

}