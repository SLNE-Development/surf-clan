package dev.slne.surf.clan.api.common.clan

import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.api.common.util.ComponentResult
import kotlin.reflect.KClass

interface HasAuthorization {
    suspend fun <Action : ClanAction<Arguments>, Arguments : Any> authorize(
        actionClass: KClass<out Action>,
        player: ClanPlayer,
        arguments: Arguments,
    ): ComponentResult

    fun hasPermission(clanPlayer: ClanPlayer, permission: ClanPermission): Boolean
}

suspend inline fun <reified Action : ClanAction<Arguments>, Arguments : Any> HasAuthorization.authorize(
    player: ClanPlayer,
    arguments: Arguments,
): ComponentResult = authorize(Action::class, player, arguments)