package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.command.clanDeletedMessage

fun CommandAPICommand.clanDeleteCommand() = subcommand("delete") {
    withPermission(ClanPermissions.CLAN_ADMIN_DELETE_COMMAND)

    argument(ClanByClanTagArgument("clan"))

    anyExecutorSuspend { sender, arguments ->
        val clan = arguments.awaiting<Clan>("clan") as ClanImpl

        val deleted = clan.delete()
        sender.sendMessage(clanDeletedMessage(clan, deleted))
    }
}