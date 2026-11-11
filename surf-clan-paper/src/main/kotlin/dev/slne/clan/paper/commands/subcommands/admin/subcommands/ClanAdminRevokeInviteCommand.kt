package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.commands.arguments.ClanInviteeNameArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.clan.core.client.command.adminRevokeInvite

fun CommandAPICommand.clanAdminRevokeInviteCommand() = subcommand("revokeinvite") {
    withPermission(ClanPermissions.CLAN_ADMIN_REVOKE_INVITE_COMMAND)

    argument(ClanByClanTagArgument("clan"))
    argument(ClanInviteeNameArgument("player", "clan"))

    anyExecutorSuspend { sender, args ->
        val clan = args.awaiting<Clan>("clan")
        val target = args.getUnchecked<String>("player")!!

        sender.sendClanAdminResult(adminRevokeInvite(sender.clanAdminActor(), clan, target))
    }
}
