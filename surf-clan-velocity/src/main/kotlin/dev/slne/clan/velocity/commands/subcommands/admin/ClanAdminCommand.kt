package dev.slne.clan.velocity.commands.subcommands.admin

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.velocity.commands.subcommands.admin.subcommands.clanInvalidateAllCachesCommand
import dev.slne.clan.velocity.commands.subcommands.admin.subcommands.clanReloadCommand
import dev.slne.clan.velocity.permission.ClanPermissions

fun CommandAPICommand.clanAdminCommand() = subcommand("admin") {
    withPermission(ClanPermissions.CLAN_ADMIN_COMMAND)

    clanReloadCommand()
    clanInvalidateAllCachesCommand()
}