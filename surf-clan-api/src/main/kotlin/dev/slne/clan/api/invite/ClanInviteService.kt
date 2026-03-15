package dev.slne.clan.api.invite

import dev.slne.clan.api.util.InternalClanApi
import dev.slne.surf.surfapi.core.api.util.requiredService
import java.util.*

private val service = requiredService<ClanInviteService>()

@InternalClanApi
interface ClanInviteService {
    suspend fun getPendingInvitesByPlayer(invited: UUID): List<ClanInvite>
    suspend fun getPendingInviteByPlayerAndClanName(invited: UUID, clanName: String): ClanInvite?

    companion object : ClanInviteService by service {
        val INSTANCE get() = service
    }
}