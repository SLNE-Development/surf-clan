package dev.slne.clan.paper.permission

import dev.slne.surf.api.paper.permission.PermissionRegistry
import dev.slne.surf.clan.core.client.permission.ClanPermissions as SharedClanPermissions

object ClanPermissions : PermissionRegistry() {
    val CLAN_COMMAND = create(SharedClanPermissions.CLAN_COMMAND)
    val CLAN_CREATE_COMMAND = create(SharedClanPermissions.CLAN_CREATE_COMMAND)
    val CLAN_DISBAND_COMMAND = create(SharedClanPermissions.CLAN_DISBAND_COMMAND)
    val CLAN_LEAVE_COMMAND = create(SharedClanPermissions.CLAN_LEAVE_COMMAND)
    val CLAN_INFO_COMMAND = create(SharedClanPermissions.CLAN_INFO_COMMAND)
    val CLAN_WHOIS_COMMAND = create(SharedClanPermissions.CLAN_WHOIS_COMMAND)
    val CLAN_SET_DISCORD_COMMAND = create(SharedClanPermissions.CLAN_SET_DISCORD_COMMAND)
    val CLAN_INVITE_COMMAND = create(SharedClanPermissions.CLAN_INVITE_COMMAND)
    val CLAN_ACCEPT_INVITE_COMMAND = create(SharedClanPermissions.CLAN_ACCEPT_INVITE_COMMAND)
    val CLAN_DENY_INVITE_COMMAND = create(SharedClanPermissions.CLAN_DENY_INVITE_COMMAND)
    val CLAN_PROMOTE_MEMBER_COMMAND = create(SharedClanPermissions.CLAN_PROMOTE_MEMBER_COMMAND)
    val CLAN_DEMOTE_MEMBER_COMMAND = create(SharedClanPermissions.CLAN_DEMOTE_MEMBER_COMMAND)
    val CLAN_KICK_MEMBER_COMMAND = create(SharedClanPermissions.CLAN_KICK_MEMBER_COMMAND)
    val CLAN_VIEW_MEMBERS_COMMAND = create(SharedClanPermissions.CLAN_VIEW_MEMBERS_COMMAND)

    val CLAN_OPTIONS_COMMAND = create(SharedClanPermissions.CLAN_OPTIONS_COMMAND)
    val CLAN_OPTIONS_TAG_COLOR_COMMAND =
        create(SharedClanPermissions.CLAN_OPTIONS_TAG_COLOR_COMMAND)
    val CLAN_OPTIONS_TAG_COLOR_BACKGROUND_COMMAND =
        create(SharedClanPermissions.CLAN_OPTIONS_TAG_COLOR_BACKGROUND_COMMAND)
    val CLAN_OPTIONS_TAG_COLOR_FOREGROUND_COMMAND =
        create(SharedClanPermissions.CLAN_OPTIONS_TAG_COLOR_FOREGROUND_COMMAND)
    val CLAN_OPTIONS_TAG_COLOR_SHADOW_COMMAND =
        create(SharedClanPermissions.CLAN_OPTIONS_TAG_COLOR_SHADOW_COMMAND)

    val CLAN_PLAYER_COMMAND = create(SharedClanPermissions.CLAN_PLAYER_COMMAND)
    val CLAN_PLAYER_SETTINGS_COMMAND = create(SharedClanPermissions.CLAN_PLAYER_SETTINGS_COMMAND)
    val CLAN_PLAYER_SETTINGS_INVITE_COMMAND =
        create(SharedClanPermissions.CLAN_PLAYER_SETTINGS_INVITE_COMMAND)

    val CLAN_ADMIN_COMMAND = create(SharedClanPermissions.CLAN_ADMIN_COMMAND)
    val CLAN_ADMIN_RELOAD_COMMAND = create(SharedClanPermissions.CLAN_ADMIN_RELOAD_COMMAND)
    val CLAN_ADMIN_INVALIDATE_CACHE_COMMAND =
        create(SharedClanPermissions.CLAN_ADMIN_INVALIDATE_CACHE_COMMAND)
    val CLAN_ADMIN_DELETE_COMMAND = create(SharedClanPermissions.CLAN_ADMIN_DELETE_COMMAND)

    val CLAN_LIST_COMMAND = create(SharedClanPermissions.CLAN_LIST_COMMAND)
    val CLAN_CHAT_COMMAND = create(SharedClanPermissions.CLAN_CHAT_COMMAND)
}
