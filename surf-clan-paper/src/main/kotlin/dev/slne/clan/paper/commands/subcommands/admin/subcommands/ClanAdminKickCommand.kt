package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.commands.arguments.ClanMemberNameArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.clan.core.client.command.adminKick

fun CommandAPICommand.clanAdminKickCommand() = subcommand("kick") {
    withPermission(ClanPermissions.CLAN_ADMIN_KICK_COMMAND)

    argument(ClanByClanTagArgument("clan"))
    argument(ClanMemberNameArgument("player", "clan"))

    anyExecutorSuspend { sender, args ->
        val clan = args.awaiting<Clan>("clan")
        val target = args.getUnchecked<String>("player")!!

        sender.sendClanAdminResult(adminKick(sender.clanAdminActor(), clan, target))
    }
}
