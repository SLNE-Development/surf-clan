package dev.slne.clan.api.member

import kotlinx.serialization.Serializable

/**
 * Represents the result of attempting to add a member to a clan.
 *
 * This sealed interface provides the outcomes when trying to add a player as a clan member.
 *
 * @see ClanMember
 */
@Serializable
sealed interface ClanMemberAddResult {
    /**
     * Indicates that the member was successfully added to the clan.
     *
     * @property member the newly added clan member
     */
    @Serializable
    data class Success(val member: ClanMember) : ClanMemberAddResult

    /**
     * Indicates that the player is already a member of this or another clan.
     */
    @Serializable
    data object AlreadyMember : ClanMemberAddResult
}