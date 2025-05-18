package dev.slne.surf.clan.api.common.member

import dev.slne.surf.clan.api.common.permission.ClanPermission
import java.time.ZonedDateTime
import java.util.*

interface ClanMember {

    /**
     * The unique id of the member.
     */
    val uuid: UUID

    /**
     * The role of the member.
     */
    var role: ClanMemberRole

    /**
     * The member that added this member if any
     */
    val addedBy: ClanMember?

    /**
     * The time this member was added to the clan.
     */
    val createdAt: ZonedDateTime?

    /**
     * The time this member was last updated.
     */
    val updatedAt: ZonedDateTime?

    /**
     * Checks if this member has the given permission.
     *
     * @param clanPermission The permission to check.
     * @return True if this member has the permission, false otherwise.
     */
    fun hasPermission(clanPermission: ClanPermission): Boolean

}