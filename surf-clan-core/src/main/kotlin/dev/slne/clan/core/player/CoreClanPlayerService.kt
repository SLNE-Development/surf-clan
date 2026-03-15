package dev.slne.clan.core.player

import dev.slne.clan.api.player.ClanPlayerService

interface CoreClanPlayerService : ClanPlayerService {
    suspend fun invalidateCaches()
    suspend fun changeAcceptsClanInvites(
        playerImpl: ClanPlayerImpl,
        acceptsClanInvites: Boolean
    ): Boolean

    companion object : CoreClanPlayerService by ClanPlayerService.INSTANCE as CoreClanPlayerService
}