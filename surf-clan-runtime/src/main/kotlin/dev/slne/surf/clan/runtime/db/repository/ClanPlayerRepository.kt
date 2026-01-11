package dev.slne.surf.clan.runtime.db.repository

import dev.slne.clan.core.player.ClanPlayerImpl
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

interface ClanPlayerRepository {

    suspend fun findOrCreateByUuid(uuid: UUID): ClanPlayerImpl
    suspend fun changeAcceptsClanInvites(playerID: ULong, acceptsClanInvites: Boolean): Boolean

    companion object : ClanPlayerRepository by INSTANCE
}

private val INSTANCE = requiredService<ClanPlayerRepository>()