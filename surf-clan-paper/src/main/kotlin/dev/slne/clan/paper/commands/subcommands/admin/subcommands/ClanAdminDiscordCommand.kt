package dev.slne.clan.paper.commands.subcommands.admin.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.argument
import dev.jorel.commandapi.kotlindsl.greedyStringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaiting
import dev.slne.surf.api.paper.command.executors.anyExecutorSuspend
import dev.slne.surf.clan.core.client.command.DISCORD_LINK_SUGGESTIONS
import dev.slne.surf.clan.core.client.command.REMOVE_DISCORD_LINK
import dev.slne.surf.clan.core.client.command.adminSetDiscord

fun CommandAPICommand.clanAdminDiscordCommand() = subcommand("discord") {
    withPermission(ClanPermissions.CLAN_ADMIN_DISCORD_COMMAND)

    subcommand("set") {
        argument(ClanByClanTagArgument("clan"))
        greedyStringArgument("link") {
            includeSuggestions(ArgumentSuggestions.strings(DISCORD_LINK_SUGGESTIONS - REMOVE_DISCORD_LINK))
        }

        anyExecutorSuspend { sender, args ->
            val clan = args.awaiting<Clan>("clan")
            val link = args.getUnchecked<String>("link")!!

            sender.sendClanAdminResult(adminSetDiscord(sender.clanAdminActor(), clan, link))
        }
    }

    subcommand("clear") {
        argument(ClanByClanTagArgument("clan"))

        anyExecutorSuspend { sender, args ->
            val clan = args.awaiting<Clan>("clan")

            sender.sendClanAdminResult(adminSetDiscord(sender.clanAdminActor(), clan, null))
        }
    }
}
