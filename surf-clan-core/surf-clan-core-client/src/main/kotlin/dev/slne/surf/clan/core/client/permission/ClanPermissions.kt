package dev.slne.surf.clan.core.client.permission

/**
 * Platform-neutral registry of all permission node strings used by surf-clan.
 */
object ClanPermissions {
    private const val PREFIX = "surf.clan"

    private const val COMMAND = "$PREFIX.command"

    const val CLAN_COMMAND = "$COMMAND.clan"
    const val CLAN_CREATE_COMMAND = "$CLAN_COMMAND.create"
    const val CLAN_DISBAND_COMMAND = "$CLAN_COMMAND.disband"
    const val CLAN_LEAVE_COMMAND = "$CLAN_COMMAND.leave"
    const val CLAN_INFO_COMMAND = "$CLAN_COMMAND.info"
    const val CLAN_WHOIS_COMMAND = "$CLAN_COMMAND.whois"
    const val CLAN_SET_DISCORD_COMMAND = "$CLAN_COMMAND.setDiscord"
    const val CLAN_INVITE_COMMAND = "$CLAN_COMMAND.invite"
    const val CLAN_ACCEPT_INVITE_COMMAND = "$CLAN_COMMAND.acceptInvite"
    const val CLAN_DENY_INVITE_COMMAND = "$CLAN_COMMAND.denyInvite"
    const val CLAN_PROMOTE_MEMBER_COMMAND = "$CLAN_COMMAND.promoteMember"
    const val CLAN_DEMOTE_MEMBER_COMMAND = "$CLAN_COMMAND.demoteMember"
    const val CLAN_KICK_MEMBER_COMMAND = "$CLAN_COMMAND.kickMember"
    const val CLAN_VIEW_MEMBERS_COMMAND = "$CLAN_COMMAND.viewMembers"

    const val CLAN_OPTIONS_COMMAND = "$COMMAND.options"
    const val CLAN_OPTIONS_TAG_COLOR_COMMAND = "$CLAN_OPTIONS_COMMAND.tagColor"
    const val CLAN_OPTIONS_TAG_COLOR_BACKGROUND_COMMAND =
        "$CLAN_OPTIONS_TAG_COLOR_COMMAND.background"
    const val CLAN_OPTIONS_TAG_COLOR_FOREGROUND_COMMAND =
        "$CLAN_OPTIONS_TAG_COLOR_COMMAND.foreground"
    const val CLAN_OPTIONS_TAG_COLOR_SHADOW_COMMAND = "$CLAN_OPTIONS_TAG_COLOR_COMMAND.shadow"

    const val CLAN_PLAYER_COMMAND = "$PREFIX.player"
    const val CLAN_PLAYER_SETTINGS_COMMAND = "$CLAN_PLAYER_COMMAND.settings"
    const val CLAN_PLAYER_SETTINGS_INVITE_COMMAND = "$CLAN_PLAYER_SETTINGS_COMMAND.invite"

    const val CLAN_ADMIN_COMMAND = "$PREFIX.admin"
    const val CLAN_ADMIN_RELOAD_COMMAND = "$CLAN_ADMIN_COMMAND.reload"
    const val CLAN_ADMIN_INVALIDATE_CACHE_COMMAND = "$CLAN_ADMIN_COMMAND.invalidateCache"
    const val CLAN_ADMIN_DELETE_COMMAND = "$CLAN_ADMIN_COMMAND.delete"

    const val CLAN_LIST_COMMAND = "$PREFIX.list"
    const val CLAN_CHAT_COMMAND = "$PREFIX.chat"
}
