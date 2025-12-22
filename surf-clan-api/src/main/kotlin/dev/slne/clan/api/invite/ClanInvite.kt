package dev.slne.clan.api.invite

import java.time.LocalDateTime
import java.util.*

data class ClanInvite(
    val invited: UUID,
    val invitedByUuid: UUID,

    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime?
)