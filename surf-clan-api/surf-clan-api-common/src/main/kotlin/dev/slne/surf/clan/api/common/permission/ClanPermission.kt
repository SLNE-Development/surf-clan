package dev.slne.surf.clan.api.common.permission

enum class ClanPermission {
    /**
     * Permission to permanently disband the clan
     */
    DISBAND,

    /**
     * Permission to invite a new member to the clan
     */
    INVITE,

    /**
     * Permission to kick a member from the clan
     */
    KICK,

    /**
     * Permission to promote a member to a higher role, which is not above the users current role
     */
    PROMOTE,

    /**
     * Permission to demote a member to a lower role
     */
    DEMOTE,

    /**
     * Permission to change the clan discord link
     */
    DISCORD,

    /**
     * Permission to change the clan tag color
     */
    OPTIONS_TAG_COLOR
}