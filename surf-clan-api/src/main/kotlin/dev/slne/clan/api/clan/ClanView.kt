package dev.slne.clan.api.clan

import dev.slne.clan.api.invite.ClanInviteView
import dev.slne.clan.api.member.ClanMemberView
import dev.slne.clan.api.permission.ClanPermission
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import org.jetbrains.annotations.ApiStatus
import java.time.OffsetDateTime
import java.util.*

/**
 * Read-only view of a clan.
 *
 * This interface provides immutable access to a clan's information and members
 * without allowing modifications.
 *
 * @see Clan
 */
@ApiStatus.NonExtendable
interface ClanView {
    /**
     * The unique identifier of this clan.
     */
    val uuid: UUID

    /**
     * The full name of this clan.
     */
    val name: String

    /**
     * The short tag identifier of this clan, typically 3-4 characters.
     */
    val tag: String

    /**
     * The UUID of the player who created this clan.
     */
    val createdByUuid: UUID

    /**
     * The optional description text of this clan.
     *
     * Returns `null` if no description has been set.
     */
    val description: String?

    /**
     * The Discord invite link for this clan.
     *
     * Returns `null` if no Discord invite has been set.
     */
    val discordInvite: String?

    val clanTagColor: ClanTagColor

    /**
     * An immutable set of all members in this clan.
     */
    val members: Set<ClanMemberView>

    /**
     * The timestamp when this clan was last updated.
     */
    val updatedAt: OffsetDateTime

    /**
     * The timestamp when this clan was created.
     */
    val createdAt: OffsetDateTime

    /**
     * Retrieves all pending invitations sent by this clan.
     *
     * @return a set of pending invitations, or an empty set if none exist
     */
    suspend fun getPendingInvites(): Set<ClanInviteView>

    /**
     * Checks if a player is a member of this clan.
     *
     * @param uuid the UUID of the player to check
     * @return `true` if the player is a member, `false` otherwise
     */
    fun isMember(uuid: UUID): Boolean

    /**
     * Checks if a member has a specific permission based on their role.
     *
     * @param uuid the UUID of the member to check
     * @param permission the permission to verify
     * @return `true` if the member has the permission, `false` otherwise
     */
    fun hasMemberPermission(uuid: UUID, permission: ClanPermission): Boolean

    /**
     * Retrieves a specific member of this clan.
     *
     * @param uuid the UUID of the member
     * @return the [ClanMemberView] if found, or `null` if the player is not a member
     */
    fun getMember(uuid: UUID): ClanMemberView?

    /**
     * Creates a formatted component displaying the clan's tag using bitmap fonts.
     *
     * This method converts the clan tag into a component using a bitmap font provider,
     * with the configured clan tag color applied to the rendered text.
     *
     * @return a component representing the clan tag with bitmap font rendering
     */
    fun getRichClanTag(): Component

    /**
     * Renders the clan tag with a minimum member requirement check.
     *
     * This method returns the formatted clan tag only if the clan meets certain criteria:
     * - The tag must not be blank
     * - The clan must have at least [minSize] members, OR the tag must be whitelisted
     *
     * If these conditions are not met, an empty component is returned. This is typically used
     * to prevent small or new clans from displaying tags until they reach a certain size.
     *
     * @param minSize the minimum number of members required for the tag to be displayed (default: 0)
     * @return the formatted clan tag component if criteria are met, otherwise an empty component
     */
    fun renderClanTag(minSize: Int = 0): Component

    fun broadcast(message: Component)
}