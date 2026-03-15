package dev.slne.clan.api.clan

import dev.slne.clan.api.clan.listener.ClanListener
import dev.slne.clan.api.util.InternalClanApi
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

private val service = requiredService<ClanService>()

@InternalClanApi
interface ClanService {
    fun registerListener(listener: ClanListener)
    fun unregisterListener(listener: ClanListener)

    suspend fun findClanByPlayer(playerUuid: UUID): Clan?
    suspend fun findClanByUuid(clanUuid: UUID): Clan?
    suspend fun findClanByTag(tag: String): Clan?

    fun validateClanNameAndTag(name: String, tag: String): ClanValidationResult
    suspend fun createClan(properties: ClanCreateBuilder): ClanCreationResult

    companion object : ClanService by service {
        val INSTANCE get() = service
    }
}