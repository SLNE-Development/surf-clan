package dev.slne.clan.api.player

import dev.slne.clan.api.util.InternalClanApi
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.UUID

@InternalClanApi
interface ClanPlayerService {

    suspend fun findByUuid(uuid: UUID): ClanPlayer

    companion object {
        val instance = requiredService<ClanPlayerService>()
    }
}