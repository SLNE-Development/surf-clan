package dev.slne.surf.clan.api.common

import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.results.ClanCreateResult

interface ClanApi {

    suspend fun createClan(
        name: String,
        tag: String,
        createdBy: ClanPlayer
    ): Pair<ClanCreateResult, Clan?>

    suspend fun findClanByName(name: String): Clan?

    suspend fun findClanByTag(tag: String): Clan?

    suspend fun findClanByPlayer(player: ClanPlayer): Clan?

    companion object : ClanApi by instance {
        val delegate get() = instance
    }

}

private val instance = ClanInternalBridge.instance.getBean(ClanApi::class)
