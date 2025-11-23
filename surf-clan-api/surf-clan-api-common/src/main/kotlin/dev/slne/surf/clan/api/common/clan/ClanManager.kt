@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.api.common.clan

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.InternalClanApi
import org.springframework.beans.factory.getBean
import org.springframework.stereotype.Component
import java.util.*

const val CLAN_TAG_MIN_LENGTH = 4
const val CLAN_TAG_MAX_LENGTH = 4

const val CLAN_NAME_MIN_LENGTH = 4
const val CLAN_NAME_MAX_LENGTH = 16

interface ClanManager {

    fun getClanByUuid(uuid: UUID): Clan?
    fun getClanByName(name: String): Clan?
    fun getClanByTag(tag: String): Clan?

    fun getClanByPlayer(player: ClanPlayer): Clan?

    companion object : ClanManager by InternalContextHolder.context.getBean<ClanManager>() {
        operator fun get(uuid: UUID): Clan? = getClanByUuid(uuid)
    }
}