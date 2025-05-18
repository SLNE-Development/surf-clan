package dev.slne.surf.clan.api.common

import dev.slne.surf.bitmap.bitmaps.Bitmaps
import dev.slne.surf.clan.api.common.invite.ClanInvite
import dev.slne.surf.clan.api.common.member.ClanMember
import dev.slne.surf.clan.api.common.member.ClanMemberRole
import dev.slne.surf.clan.api.common.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.time.ZonedDateTime
import java.util.*

interface Clan {

    /**
     * The unique identifier of the clan.
     */
    val uuid: UUID

    /**
     * The name of the clan.
     */
    val name: String

    /**
     * The tag of the clan.
     */
    val tag: String

    /**
     * The unique identifier of the clan creator. Doesn't have to be a member of the clan anymore.
     */
    val createdBy: UUID

    /**
     * The description of the clan.
     */
    val description: String?

    /**
     * The discord invite link of the clan.
     */
    var discordInvite: String?

    /**
     * The color of the clan tag.
     */
    var clanTagColor: Bitmaps?

    /**
     * The members of the clan as an unmodifiable set.
     */
    val members: ObjectSet<ClanMember>

    /**
     * The invites of the clan as an unmodifiable set.
     */
    val invites: ObjectSet<ClanInvite>

    /**
     * The date and time when the clan was created.
     */
    val createdAt: ZonedDateTime?

    /**
     * The date and time when the clan was last updated.
     */
    val updatedAt: ZonedDateTime?

    /**
     * Adds an invite to the clan.
     *
     * @param uuid the unique identifier of the player to invite
     * @param invitedBy the unique identifier of the player who invited
     * @return true if the invite was added successfully, false otherwise
     */
    fun invite(uuid: UUID, invitedBy: UUID): Boolean

    /**
     * Removes an invite from the clan.
     *
     * @param uuid the unique identifier of the player to remove
     * @return true if the invite was removed successfully, false otherwise
     */
    fun removeInvite(uuid: UUID): Boolean

    /**
     * Checks if the given uuid is a member of the clan
     *
     * @param uuid the unique identifier of the player to check
     * @return true if the player is a member of the clan, false otherwise
     */
    fun isMember(uuid: UUID): Boolean

    /**
     * Adds a member to the clan.
     *
     * @param uuid the unique identifier of the player to add
     * @param role the role of the player in the clan
     * @param addedBy the unique identifier of the player who added the member
     * @return true if the member was added successfully, false otherwise
     */
    fun addMember(uuid: UUID, role: ClanMemberRole, addedBy: UUID): Boolean

    /**
     * Adds a member to the clan.
     *
     * @param member the member to add
     * @return true if the member was added successfully, false otherwise
     */
    fun addMember(member: ClanMember): Boolean

    /**
     * Removes a member from the clan.
     *
     * @param member the member to remove
     * @return true if the member was removed successfully, false otherwise
     */
    fun removeMember(member: ClanMember): Boolean

    /**
     * Checks if the given player has the given permission.
     *
     * @param clanMember the member to check
     * @param permission the permission to check
     * @return true if the player has the permission, false otherwise
     */
    fun hasPermission(clanMember: ClanMember, permission: ClanPermission): Boolean

    /**
     * Gets a clan member by the given clan player.
     *
     * @param clanPlayer the clan player to get the member for
     * @return the clan member or null if not found because the player is not a member of the clan
     */
    fun getMember(clanPlayer: ClanPlayer): ClanMember?

    /**
     * Translates the clan tag to the correct glyphs.
     *
     * @return the translated clan tag
     */
    fun getTranslatedClanTag(): String

}