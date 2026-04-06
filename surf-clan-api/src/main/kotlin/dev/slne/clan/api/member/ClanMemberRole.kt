package dev.slne.clan.api.member

import dev.slne.clan.api.permission.ClanPermission
import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.adventure.text
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.ComponentLike
import java.util.*

/**
 * Represents the hierarchical roles within a clan.
 *
 * Each role has an associated display name and a set of permissions that determine
 * what actions members with that role can perform. Roles are ordered from lowest
 * to highest authority: [MEMBER], [OFFICER], [LEADER], [OWNER].
 *
 * @property displayName the localized display name of the role as an Adventure component
 * @property permissions the set of permissions granted to this role
 */
enum class ClanMemberRole(
    val displayName: Component,
    vararg permissions: ClanPermission,
) : ComponentLike {

    /**
     * Basic member role with no special permissions.
     */
    MEMBER(
        text("Mitglied", Colors.YELLOW)
    ),

    /**
     * Officer role with permissions to invite and kick members.
     */
    OFFICER(
        text("Offizier", Colors.GOLD),
        ClanPermission.INVITE,
        ClanPermission.KICK,
    ),

    /**
     * Leader role with extended management permissions including promotion and demotion.
     */
    LEADER(
        text("Anführer", Colors.RED),
        ClanPermission.INVITE,
        ClanPermission.KICK,
        ClanPermission.DEMOTE,
        ClanPermission.PROMOTE,
        ClanPermission.DISCORD
    ),

    /**
     * Owner role with full administrative permissions including the ability to disband the clan.
     */
    OWNER(
        text("Besitzer", Colors.RED),
        ClanPermission.DISBAND,
        ClanPermission.INVITE,
        ClanPermission.KICK,
        ClanPermission.DEMOTE,
        ClanPermission.PROMOTE,
        ClanPermission.DISCORD,
        ClanPermission.OPTIONS_TAG_COLOR
    );

    private val permissions = EnumSet.noneOf(ClanPermission::class.java).apply {
        addAll(permissions.toSet())
    }

    /**
     * Checks if there is a higher role available for promotion.
     *
     * @return `true` if this role can be promoted, `false` if it's already at the maximum non-owner level
     */
    fun hasNextRole() = this != LEADER && this != OWNER

    /**
     * Checks if there is a lower role available for demotion.
     *
     * @return `true` if this role can be demoted, `false` if it's already at the minimum level
     */
    fun hasPreviousRole() = this != MEMBER

    /**
     * Returns the next higher role in the hierarchy.
     *
     * Promotions follow this path: MEMBER → OFFICER → LEADER.
     * [LEADER] and [OWNER] cannot be promoted further and return themselves.
     *
     * @return the next role, or the current role if already at maximum
     */
    fun nextRole() = when (this) {
        MEMBER -> OFFICER
        OFFICER -> LEADER
        LEADER -> LEADER
        OWNER -> OWNER
    }

    /**
     * Returns the next lower role in the hierarchy.
     *
     * Demotions follow this path: LEADER → OFFICER → MEMBER.
     * [MEMBER] and [OWNER] cannot be demoted further and return themselves.
     *
     * @return the previous role, or the current role if already at minimum
     */
    fun previousRole() = when (this) {
        MEMBER -> MEMBER
        OFFICER -> MEMBER
        LEADER -> OFFICER
        OWNER -> OWNER
    }

    /**
     * Checks if this role has the specified permission.
     *
     * @param permission the permission to check
     * @return `true` if this role includes the permission, `false` otherwise
     */
    fun hasPermission(permission: ClanPermission) = permissions.contains(permission)

    /**
     * Returns the display name component for use with Adventure text APIs.
     *
     * @return the display name as a component
     */
    override fun asComponent() = displayName
}