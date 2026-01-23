package dev.slne.clan.api.invite

import org.jetbrains.annotations.ApiStatus
import java.util.*

@ApiStatus.NonExtendable
interface ClanInvite : ClanInviteView {
    suspend fun accept(): ClanInviteAcceptResult
    suspend fun revoke(): Boolean

    fun view(): ClanInviteView

    companion object {
        suspend fun pendingInvitesByPlayer(invited: UUID): List<ClanInvite> =
            ClanInviteService.instance.getPendingInvitesByPlayer(invited)

        suspend fun pendingInviteByPlayerAndClanName(invited: UUID, clanName: String): ClanInvite? =
            ClanInviteService.instance.getPendingInviteByPlayerAndClanName(invited, clanName)
    }
}