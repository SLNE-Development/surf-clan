package dev.slne.clan.api.player

import dev.slne.clan.api.util.InternalClanApi
import dev.slne.surf.api.core.util.requiredService
import java.util.*

private val service = requiredService<ClanPlayerService>()

@InternalClanApi
interface ClanPlayerService {
    suspend fun findByUuid(uuid: UUID): ClanPlayer

    companion object : ClanPlayerService by service {
        val INSTANCE get() = service
    }
}