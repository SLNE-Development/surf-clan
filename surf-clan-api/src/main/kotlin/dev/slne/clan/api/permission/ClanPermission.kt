package dev.slne.clan.api.permission

/**
 * Represents permissions that can be granted to clan members based on their role.
 *
 * Each permission controls access to specific clan management actions. Permissions are
 * typically associated with [dev.slne.clan.api.member.ClanMemberRole] instances.
 *
 * @see dev.slne.clan.api.member.ClanMemberRole
 */
enum class ClanPermission {
    /**
     * Permission to permanently delete the clan.
     *
     * This is the highest-level permission, typically reserved for the clan owner.
     */
    DISBAND,

    /**
     * Permission to invite new members to the clan.
     */
    INVITE,

    /**
     * Permission to remove members from the clan.
     */
    KICK,

    /**
     * Permission to promote members to higher roles.
     */
    PROMOTE,

    /**
     * Permission to demote members to lower roles.
     */
    DEMOTE,

    /**
     * Permission to manage the clan's Discord integration settings.
     */
    DISCORD,

    /**
     * Permission to change the color of the clan tag.
     */
    OPTIONS_TAG_COLOR
}