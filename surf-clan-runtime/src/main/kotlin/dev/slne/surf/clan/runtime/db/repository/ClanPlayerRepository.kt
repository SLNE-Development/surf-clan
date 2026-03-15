package dev.slne.surf.clan.runtime.db.repository

import dev.slne.clan.core.player.ClanPlayerImpl
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

private val instance = requiredService<ClanPlayerRepository>()

interface ClanPlayerRepository {
    suspend fun findOrCreateByUuid(uuid: UUID): ClanPlayerImpl
    suspend fun changeAcceptsClanInvites(playerID: ULong, acceptsClanInvites: Boolean): Boolean

    companion object : ClanPlayerRepository by instance {
        val INSTANCE get() = instance
    }
}