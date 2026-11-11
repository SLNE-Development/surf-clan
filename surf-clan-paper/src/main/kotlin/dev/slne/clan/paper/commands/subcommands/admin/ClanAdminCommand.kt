package dev.slne.clan.paper.commands.subcommands.admin

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.paper.commands.subcommands.admin.subcommands.*
import dev.slne.clan.paper.permission.ClanPermissions

fun CommandAPICommand.clanAdminCommand() = subcommand("admin") {
    withPermission(ClanPermissions.CLAN_ADMIN_COMMAND)

    clanReloadCommand()
    clanInvalidateAllCachesCommand()
    clanDeleteCommand()

    clanAdminSetRoleCommand()
    clanAdminTransferOwnerCommand()
    clanAdminKickCommand()
    clanAdminAddCommand()
    clanAdminDescriptionCommand()
    clanAdminDiscordCommand()
    clanAdminInvitesCommand()
    clanAdminRevokeInviteCommand()
}
