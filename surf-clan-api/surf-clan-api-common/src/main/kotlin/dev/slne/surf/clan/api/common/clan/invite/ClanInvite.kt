package dev.slne.surf.clan.api.common.clan.invite

import dev.slne.surf.clan.api.common.player.ClanPlayer
import java.time.ZonedDateTime
import java.util.*

interface ClanInvite {
    val invitedUuid: UUID
    val invitedByUuid: UUID

    suspend fun invited(): ClanPlayer = ClanPlayer[invitedUuid]
    suspend fun invitedBy(): ClanPlayer = ClanPlayer[invitedByUuid]

    val createdAt: ZonedDateTime
    val updatedAt: ZonedDateTime
}
