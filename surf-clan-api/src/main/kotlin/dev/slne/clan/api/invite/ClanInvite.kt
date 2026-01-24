package dev.slne.clan.api.invite

import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * Represents a mutable clan invitation with the ability to perform actions.
 *
 * This interface extends [ClanInviteView] and provides methods for accepting or revoking
 * clan invitations.
 *
 * @see ClanInviteView
 * @see ClanInviteAcceptResult
 */
@ApiStatus.NonExtendable
interface ClanInvite : ClanInviteView {

    /**
     * Accepts this clan invitation and adds the invited player to the clan.
     *
     * This operation is performed asynchronously and may involve database updates and
     * validation checks.
     *
     * @return a [ClanInviteAcceptResult] indicating whether the invitation was accepted
     *         or if the player is already in the clan
     */
    suspend fun accept(): ClanInviteAcceptResult

    /**
     * Revokes this clan invitation, making it no longer valid.
     *
     * Once revoked, the invited player can no longer accept this invitation.
     *
     * @return `true` if the invitation was successfully revoked, `false` otherwise
     */
    suspend fun revoke(): Boolean

    /**
     * Returns a read-only view of this clan invitation.
     *
     * Use this method when you need to pass invitation information to code that should not
     * be able to modify or act upon the invitation.
     *
     * @return an immutable view of this invitation
     */
    fun view(): ClanInviteView

    companion object {
        /**
         * Retrieves all pending invitations for a specific player.
         *
         * @param invited the UUID of the invited player
         * @return a list of all pending invitations for the player, or an empty list if none exist
         */
        suspend fun pendingInvitesByPlayer(invited: UUID): List<ClanInvite> =
            ClanInviteService.instance.getPendingInvitesByPlayer(invited)

        /**
         * Retrieves a specific pending invitation for a player by clan name.
         *
         * @param invited the UUID of the invited player
         * @param clanName the name of the clan that sent the invitation
         * @return the pending invitation, or `null` if no matching invitation exists
         */
        suspend fun pendingInviteByPlayerAndClanName(invited: UUID, clanName: String): ClanInvite? =
            ClanInviteService.instance.getPendingInviteByPlayerAndClanName(invited, clanName)
    }
}