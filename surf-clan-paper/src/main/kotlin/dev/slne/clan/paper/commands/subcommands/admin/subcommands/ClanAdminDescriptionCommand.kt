package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.greedyStringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.clan.core.client.command.adminSetDescription

fun CommandAPICommand.clanAdminDescriptionCommand() = subcommand("description") {
    withPermission(ClanPermissions.CLAN_ADMIN_DESCRIPTION_COMMAND)

    subcommand("set") {
        argument(ClanByClanTagArgument("clan"))
        greedyStringArgument("text")

        anyExecutorSuspend { sender, args ->
            val clan = args.awaiting<Clan>("clan")
            val text = args.getUnchecked<String>("text")!!

            sender.sendClanAdminResult(adminSetDescription(sender.clanAdminActor(), clan, text))
        }
    }

    subcommand("clear") {
        argument(ClanByClanTagArgument("clan"))

        anyExecutorSuspend { sender, args ->
            val clan = args.awaiting<Clan>("clan")

            sender.sendClanAdminResult(adminSetDescription(sender.clanAdminActor(), clan, null))
        }
    }
}
