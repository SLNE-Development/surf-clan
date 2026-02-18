package dev.slne.clan.paper.permission

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object ClanPermissions : PermissionRegistry() {
    private const val PREFIX = "surf.clan"

    private const val COMMAND = "$PREFIX.command"

    val CLAN_COMMAND = create("$COMMAND.clan")
    val CLAN_CREATE_COMMAND = create("$CLAN_COMMAND.create")
    val CLAN_DISBAND_COMMAND = create("$CLAN_COMMAND.disband")
    val CLAN_LEAVE_COMMAND = create("$CLAN_COMMAND.leave")
    val CLAN_INFO_COMMAND = create("$CLAN_COMMAND.info")
    val CLAN_SET_DISCORD_COMMAND = create("$CLAN_COMMAND.setDiscord")
    val CLAN_INVITE_COMMAND = create("$CLAN_COMMAND.invite")
    val CLAN_ACCEPT_INVITE_COMMAND = create("$CLAN_COMMAND.acceptInvite")
    val CLAN_DENY_INVITE_COMMAND = create("$CLAN_COMMAND.denyInvite")
    val CLAN_PROMOTE_MEMBER_COMMAND = create("$CLAN_COMMAND.promoteMember")
    val CLAN_DEMOTE_MEMBER_COMMAND = create("$CLAN_COMMAND.demoteMember")
    val CLAN_KICK_MEMBER_COMMAND = create("$CLAN_COMMAND.kickMember")
    val CLAN_VIEW_MEMBERS_COMMAND = create("$CLAN_COMMAND.viewMembers")

    val CLAN_OPTIONS_COMMAND = create("$COMMAND.options")
    val CLAN_OPTIONS_TAG_COLOR_COMMAND = create("$CLAN_OPTIONS_COMMAND.tagColor")

    val CLAN_PLAYER_COMMAND = create("$PREFIX.player")
    val CLAN_PLAYER_SETTINGS_COMMAND = create("$CLAN_PLAYER_COMMAND.settings")
    val CLAN_PLAYER_SETTINGS_INVITE_COMMAND = create("$CLAN_PLAYER_SETTINGS_COMMAND.invite")

    val CLAN_ADMIN_COMMAND = create("$PREFIX.admin")
    val CLAN_ADMIN_RELOAD_COMMAND = create("$CLAN_ADMIN_COMMAND.reload")
    val CLAN_ADMIN_INVALIDATE_CACHE_COMMAND = create("$CLAN_ADMIN_COMMAND.invalidateCache")

    val CLAN_LIST_COMMAND = create("$PREFIX.list")
}