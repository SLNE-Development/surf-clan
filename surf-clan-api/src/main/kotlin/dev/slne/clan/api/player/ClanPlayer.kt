package dev.slne.clan.api.player

import java.util.*

interface ClanPlayer {
    val uuid: UUID
    val acceptsClanInvites: Boolean

    suspend fun setAcceptsClanInvites(acceptsClanInvites: Boolean): Boolean

    companion object {
        suspend fun byUuid(uuid: UUID): ClanPlayer = ClanPlayerService.instance.findByUuid(uuid)
    }
}