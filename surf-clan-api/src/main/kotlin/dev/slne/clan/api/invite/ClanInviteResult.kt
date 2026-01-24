package dev.slne.clan.api.invite

/**
 * Represents the result of attempting to invite a player to a clan.
 *
 * This sealed interface provides various outcomes when trying to send a clan invitation,
 * including success cases and different failure scenarios.
 *
 * @see ClanInvite
 */
sealed interface ClanInviteResult {
    /**
     * Indicates that the invitation was successfully sent.
     *
     * @property invite the newly created invitation
     */
    data class Success(val invite: ClanInvite) : ClanInviteResult

    /**
     * Indicates that the player already has a pending invitation from this clan.
     */
    data object AlreadyInvited : ClanInviteResult

    /**
     * Indicates that the player is already a member of this or another clan.
     */
    data object AlreadyInClan : ClanInviteResult

    /**
     * Indicates that the player has disabled receiving clan invitations.
     */
    data object InvitationsDisabled : ClanInviteResult
}