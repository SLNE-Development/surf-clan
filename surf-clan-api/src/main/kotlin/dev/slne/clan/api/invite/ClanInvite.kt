package dev.slne.clan.api.invite

import dev.slne.clan.api.clan.Clan
import java.time.LocalDateTime
import java.util.*

interface ClanInvite {
    val invited: UUID
    val invitedBy: UUID

    val createdAt: LocalDateTime

    suspend fun getClan(): Clan?
    suspend fun getClanOrThrow(): Clan

    suspend fun accept(): ClanInviteAcceptResult
    suspend fun revoke(): Boolean

    companion object {
        suspend fun pendingInvitesByPlayer(invited: UUID): List<ClanInvite> =
            ClanInviteService.instance.getPendingInvitesByPlayer(invited)

        suspend fun pendingInviteByPlayerAndClanName(invited: UUID, clanName: String): ClanInvite? =
            ClanInviteService.instance.getPendingInviteByPlayerAndClanName(invited, clanName)
    }
}