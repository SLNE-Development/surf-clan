package dev.slne.clan.api.player

import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * Represents a player in the clan system.
 *
 * This interface provides access to player-specific data and settings related to clan functionality.
 */
@ApiStatus.NonExtendable
interface ClanPlayer {

    /**
     * The unique identifier of this player.
     */
    val uuid: UUID

    /**
     * Indicates whether this player accepts clan invitations.
     *
     * When set to `false`, the player will not receive or be able to accept clan invites.
     */
    val acceptsClanInvites: Boolean

    /**
     * Updates the player's preference for accepting clan invitations.
     *
     * This suspend function allows changing whether the player can receive and accept clan invites.
     * The operation is performed asynchronously and may involve database updates.
     *
     * @param acceptsClanInvites `true` to allow the player to receive clan invites, `false` to block them
     * @return `true` if the update was successful, `false` otherwise
     */
    suspend fun setAcceptsClanInvites(acceptsClanInvites: Boolean): Boolean

    companion object {
        /**
         * Retrieves a [ClanPlayer] instance by their unique identifier.
         *
         * @param uuid the unique identifier of the player
         * @return the [ClanPlayer] instance associated with the given UUID
         */
        suspend fun byUuid(uuid: UUID): ClanPlayer = ClanPlayerService.findByUuid(uuid)
    }
}