package dev.slne.surf.clan.api.common.clan

import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.api.common.util.ClanActionExtraArguments
import dev.slne.surf.clan.api.common.util.ComponentResult
import kotlin.reflect.KClass

interface HasAuthorization {
    suspend fun authorize(
        actionClass: KClass<out ClanAction>,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments = ClanActionExtraArguments.EMPTY
    ): ComponentResult

    suspend fun executeAuthorizing(
        actionClass: KClass<out ClanAction>,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments = ClanActionExtraArguments.EMPTY,
        action: suspend () -> ComponentResult
    ): ComponentResult
    
    fun hasPermission(clanPlayer: ClanPlayer, permission: ClanPermission): Boolean
}

suspend inline fun <reified A : ClanAction> HasAuthorization.authorize(
    player: ClanPlayer,
    arguments: ClanActionExtraArguments = ClanActionExtraArguments.EMPTY
) = authorize(A::class, player, arguments)

suspend inline fun <reified A : ClanAction> HasAuthorization.authorize(
    player: ClanPlayer,
    vararg args: Pair<String, Any?>
) = authorize(A::class, player, ClanActionExtraArguments(*args))

suspend inline fun <reified A : ClanAction> HasAuthorization.executeAuthorizing(
    player: ClanPlayer,
    arguments: ClanActionExtraArguments = ClanActionExtraArguments.EMPTY,
    noinline action: suspend () -> ComponentResult
) = executeAuthorizing(A::class, player, arguments, action)

suspend inline fun <reified A : ClanAction> HasAuthorization.executeAuthorizing(
    player: ClanPlayer,
    vararg args: Pair<String, Any?>,
    noinline action: suspend () -> ComponentResult
) = executeAuthorizing(A::class, player, ClanActionExtraArguments(*args), action)
