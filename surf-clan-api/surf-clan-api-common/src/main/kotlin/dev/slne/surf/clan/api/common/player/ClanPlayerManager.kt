@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.api.common.player

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.util.InternalClanApi
import org.springframework.beans.factory.getBean
import java.util.*

interface ClanPlayerManager {

    suspend fun getPlayer(uuid: UUID): ClanPlayer

    // @formatter:off
    companion object : ClanPlayerManager by InternalContextHolder.context.getBean<ClanPlayerManager>() {
        suspend operator fun get(uuid: UUID): ClanPlayer = getPlayer(uuid)
    }
    // @formatter:on

}