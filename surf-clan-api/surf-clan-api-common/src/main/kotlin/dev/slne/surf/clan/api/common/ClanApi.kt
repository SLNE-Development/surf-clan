package dev.slne.surf.clan.api.common

import dev.slne.surf.clan.api.common.results.ClanCreateResult
import java.util.*

interface ClanApi {

    suspend fun findClanByName(name: String): Clan?

    suspend fun createClan(
        name: String,
        tag: String,
        createdBy: UUID
    ): Pair<ClanCreateResult, Clan?>

    companion object : ClanApi by instance {
        val delegate get() = instance
    }

}

private val instance = ClanInternalBridge.instance.getBean(ClanApi::class)
