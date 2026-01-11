package dev.slne.clan.api.clan

import dev.slne.clan.api.clan.listener.ClanListener
import dev.slne.clan.api.clan.listener.ClanUpdatedListener
import dev.slne.clan.api.util.InternalClanApi
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

@InternalClanApi
interface ClanService {

    fun registerListener(listener: ClanListener)
    fun unregisterListener(listener: ClanListener)

    suspend fun findClanByPlayer(playerUuid: UUID): Clan?
    suspend fun findClanByUuid(clanUuid: UUID): Clan?
    suspend fun findClanByTag(tag: String): Clan?

    fun validateClanNameAndTag(name: String, tag: String): ClanValidationResult
    suspend fun createClan(properties: ClanCreateBuilder): ClanCreationResult

    companion object {
        val instance = requiredService<ClanService>()
    }
}