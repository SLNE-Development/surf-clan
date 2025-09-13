@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.api.common.clan

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.InternalClanApi
import org.springframework.beans.factory.getBean
import java.util.*

interface ClanManager {

    fun getClanByUuid(uuid: UUID): Clan?
    fun getClanByName(name: String): Clan?
    fun getClanByTag(tag: String): Clan?

    fun getClanByPlayer(player: ClanPlayer): Clan?

    companion object : ClanManager by InternalContextHolder.context.getBean<ClanManager>() {
        operator fun get(uuid: UUID): Clan? = getClanByUuid(uuid)
    }
}