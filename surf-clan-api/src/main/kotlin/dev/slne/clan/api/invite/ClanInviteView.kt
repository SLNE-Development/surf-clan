package dev.slne.clan.api.invite

import dev.slne.clan.api.clan.Clan
import org.jetbrains.annotations.ApiStatus
import java.time.OffsetDateTime
import java.util.*

/**
 * Read-only view of a clan invitation.
 *
 * This interface provides immutable access to an invitation's basic information
 * without allowing modifications or actions.
 *
 * @see ClanInvite
 */
@ApiStatus.NonExtendable
interface ClanInviteView {
    /**
     * The UUID of the player who received this invitation.
     */
    val invited: UUID

    /**
     * The UUID of the clan member who sent this invitation.
     */
    val invitedBy: UUID

    /**
     * The timestamp when this invitation was created.
     */
    val createdAt: OffsetDateTime

    /**
     * Retrieves the clan associated with this invitation.
     *
     * @return the [Clan] that sent this invitation, or `null` if the clan no longer exists
     */
    suspend fun getClan(): Clan?

    /**
     * Retrieves the clan associated with this invitation.
     *
     * @return the [Clan] that sent this invitation
     * @throws NoSuchElementException if the clan no longer exists
     */
    suspend fun getClanOrThrow(): Clan
}