package dev.slne.clan.paper.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.kotlindsl.greedyStringArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.paper.commands.failCommand
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.*

fun CommandAPICommand.clanSetDiscordCommand() = subcommand("setdiscord") {
    withPermission(ClanPermissions.CLAN_SET_DISCORD_COMMAND)

    greedyStringArgument("link") {
        includeSuggestions(ArgumentSuggestions.strings(DISCORD_LINK_SUGGESTIONS))
    }

    playerExecutorSuspend { player, args ->
        val rawLink = args.getUnchecked<String>("link")!!
        val clan = Clan.byPlayer(player.uniqueId)
            ?: failCommand(Messages.NOT_IN_CLAN)

        if (!clan.hasMemberPermission(player.uniqueId, ClanPermission.DISCORD)) {
            failCommand(NO_DISCORD_PERMISSION)
        }

        val isNullLink = rawLink == REMOVE_DISCORD_LINK

        if (isNullLink) {
            clan.setDiscordInvite(null)
        } else {
            if (clan.activeMemberCount < Clan.DISCORD_LINK_REQUIRED_MEMBERS) {
                failCommand(NOT_ENOUGH_MEMBERS_FOR_DISCORD_LINK)
            }

            if (!isValidDiscordInvite(rawLink)) {
                failCommand(INVALID_DISCORD_LINK)
            }

            clan.setDiscordInvite(rawLink)
        }

        player.sendMessage(discordLinkChangedMessage(isNullLink, rawLink))
    }
}
