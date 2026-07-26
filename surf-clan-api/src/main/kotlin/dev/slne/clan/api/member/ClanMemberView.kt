package dev.slne.clan.api.member

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import org.jetbrains.annotations.ApiStatus
import java.time.OffsetDateTime
import java.util.*

/**
 * Read-only view of a clan member.
 *
 * This interface provides immutable access to a member's basic information and permissions
 * without allowing modifications.
 *
 * @see ClanMember
 */
@ApiStatus.NonExtendable
interface ClanMemberView {
    /**
     * The unique identifier of this clan member.
     */
    val uuid: UUID

    /**
     * The role of this member within the clan.
     *
     * @see ClanMemberRole
     */
    val role: ClanMemberRole

    /**
     * The UUID of the member who added this member to the clan.
     *
     * Returns `null` if the member is the clan founder or if the information is not available.
     */
    val addedBy: UUID?

    /**
     * When this member was last online anywhere on the network.
     *
     * Falls back to the date the member joined the clan when the network has no record of them,
     * so this is never `null`.
     */
    val lastActiveAt: OffsetDateTime

    /**
     * Whether this member was online within [Clan.INACTIVE_AFTER].
     */
    val isActive: Boolean
        get() = lastActiveAt.isAfter(OffsetDateTime.now().minus(Clan.INACTIVE_AFTER))

    /**
     * Checks whether this member has the specified permission based on their role.
     *
     * @param clanPermission the permission to check
     * @return `true` if the member has the permission, `false` otherwise
     */
    fun hasPermission(clanPermission: ClanPermission): Boolean
}