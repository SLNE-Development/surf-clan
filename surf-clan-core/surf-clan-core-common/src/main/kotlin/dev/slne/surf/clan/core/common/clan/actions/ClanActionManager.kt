package dev.slne.surf.clan.core.common.clan.actions

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.beans.factory.ObjectProvider
import org.springframework.stereotype.Component

@Component
class ClanActionManager(val actions: ObjectProvider<ClanActionCommon<*>>) {
    @Suppress("UNCHECKED_CAST")
    suspend fun <Action : ClanAction<Args>, Args> authorize(
        actionClass: Class<Action>,
        clan: Clan,
        player: ClanPlayer,
        arguments: Args
    ): ComponentResult {
        val action = actions.find { actionClass.isInstance(it) } as? Action
            ?: return ComponentResult.NoPolicyFound(clan, player.uuid, actionClass)

        return action.authorize(clan, player, arguments)
    }

    final suspend inline fun <reified A : ClanAction<Args>, reified Args : Any> authorize(
        clan: Clan,
        player: ClanPlayer,
        arguments: Args
    ): ComponentResult =
        withAction<A, Args>(clan, player, arguments) { it::authorize }

    final suspend inline fun <reified A : ClanAction<Args>, reified Args : Any> execute(
        clan: Clan,
        player: ClanPlayer,
        arguments: Args
    ): ComponentResult =
        withAction<A, Args>(clan, player, arguments) { it::execute }

    final inline fun <reified A : ClanAction<Args>, reified Args : Any> findAction(): A? =
        actions.filterIsInstance<A>().firstOrNull()

    final suspend inline fun <reified A : ClanAction<Args>, reified Args : Any> withAction(
        clan: Clan,
        player: ClanPlayer,
        arguments: Args,
        pick: (A) -> suspend (Clan, ClanPlayer, Args) -> ComponentResult
    ): ComponentResult {
        val action = findAction<A, Args>()
            ?: return ComponentResult.NoPolicyFound(clan, player.uuid, A::class.java)

        val actionHandler = pick(action)
        return actionHandler(clan, player, arguments)
    }
}