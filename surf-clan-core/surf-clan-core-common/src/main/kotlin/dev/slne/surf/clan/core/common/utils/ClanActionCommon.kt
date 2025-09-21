@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.core.common.utils

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.api.common.util.InternalClanApi
import dev.slne.surf.clan.core.common.clan.ClanManagerCommon
import dev.slne.surf.clan.core.common.player.ClanPlayerManagerCommon
import org.springframework.beans.factory.getBean

abstract class ClanActionCommon<T> : ClanAction<T> {
    protected val clanManager
        get() = InternalContextHolder.context.getBean<ClanManagerCommon>()

    protected val playerManager
        get() = InternalContextHolder.context.getBean<ClanPlayerManagerCommon>()
}