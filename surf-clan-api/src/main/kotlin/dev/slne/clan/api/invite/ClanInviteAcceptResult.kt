package dev.slne.clan.api.invite

import dev.slne.clan.api.clan.Clan

/**
 * Represents the result of attempting to accept a clan invitation.
 *
 * This sealed interface provides two possible outcomes when a player tries to accept
 * a clan invitation.
 *
 * @see ClanInvite.accept
 */
sealed interface ClanInviteAcceptResult {
    /**
     * Indicates that the invitation could not be accepted because the player
     * is already a member of a clan.
     */
    data object AlreadyInClan : ClanInviteAcceptResult

    /**
     * Indicates that the invitation was successfully accepted and the player
     * has joined the clan.
     *
     * @property clan the clan that the player has joined
     */
    data class Accepted(val clan: Clan) : ClanInviteAcceptResult
}