package dev.slne.surf.clan.paper.permissions

import dev.slne.surf.surfapi.bukkit.api.permission.PermissionRegistry

object Permissions : PermissionRegistry() {

    private const val PREFIX = "surf.clan"
    private const val COMMAND_PREFIX = "$PREFIX.command"

    val COMMAND_GENERIC = create("$COMMAND_PREFIX.generic")

}