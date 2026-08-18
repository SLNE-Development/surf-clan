package dev.slne.clan.minestom.command.subcommands

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPI
import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.greedyStringArgument
import dev.slne.minestom.lobby.api.command.commandapi.dsl.includeSuggestions
import dev.slne.minestom.lobby.api.command.commandapi.dsl.playerExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.surf.clan.core.client.Messages
import dev.slne.surf.clan.core.client.command.*
import dev.slne.surf.clan.core.client.permission.ClanPermissions

fun CommandAPICommand.clanSetDiscordCommand(): CommandAPICommand = withSubcommand(
    subcommand("setdiscord") {
        withPermission(ClanPermissions.CLAN_SET_DISCORD_COMMAND)

        greedyStringArgument("link") {
            includeSuggestions(*DISCORD_LINK_SUGGESTIONS.toTypedArray())
        }

        playerExecutorSuspend { player, args ->
            val rawLink = args.get<String>("link")
            val clan = Clan.byPlayer(player.uuid)
                ?: CommandAPI.failWithString(Messages.NOT_IN_CLAN)

            if (!clan.hasMemberPermission(player.uuid, ClanPermission.DISCORD)) {
                CommandAPI.failWithString(NO_DISCORD_PERMISSION)
            }

            val isNullLink = rawLink == REMOVE_DISCORD_LINK

            if (isNullLink) {
                clan.setDiscordInvite(null)
            } else {
                if (clan.activeMemberCount < Clan.DISCORD_LINK_REQUIRED_MEMBERS) {
                    CommandAPI.failWithString(NOT_ENOUGH_MEMBERS_FOR_DISCORD_LINK)
                }

                if (!isValidDiscordInvite(rawLink)) {
                    CommandAPI.failWithString(INVALID_DISCORD_LINK)
                }

                clan.setDiscordInvite(rawLink)
            }

            player.sendMessage(discordLinkChangedMessage(isNullLink, rawLink))
        }
    }
)
