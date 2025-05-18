package dev.slne.surf.clan.velocity.commands

import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.slne.surf.clan.core.common.utils.PermissionRegistry

fun clanCommand() = commandAPICommand("clan") {
    withPermission(PermissionRegistry.CLAN_COMMAND)
}