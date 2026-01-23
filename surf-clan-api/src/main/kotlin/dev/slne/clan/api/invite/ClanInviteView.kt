package dev.slne.clan.api.invite

import dev.slne.clan.api.clan.Clan
import org.jetbrains.annotations.ApiStatus
import java.time.OffsetDateTime
import java.util.UUID

@ApiStatus.NonExtendable
interface ClanInviteView {
    val invited: UUID
    val invitedBy: UUID

    val createdAt: OffsetDateTime

    suspend fun getClan(): Clan?
    suspend fun getClanOrThrow(): Clan
}