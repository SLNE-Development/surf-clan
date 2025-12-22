package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.greedyStringArgument
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.clanService
import dev.slne.clan.core.utils.ClanSettings.DISCORD_LINK_REQUIRED_MEMBERS
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class ClanSetDiscordCommand : CommandAPICommand("setdiscord") {
    init {
        withPermission("surf.clan.setdiscord")

        greedyStringArgument("discord") {
            includeSuggestions(
                ArgumentSuggestions.strings(
                    "https://discord.gg/castcrafter",
                    "https://discord.com/invite/castcrafter"
                )
            )
        }

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val playerClan = player.findClan()
                val discordFull = args.getUnchecked<String>("discord")

                if (playerClan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                if (!playerClan.hasPermission(player, ClanPermission.DISCORD)) {
                    player.sendText {
                        appendPrefix()
                        error("Du hast keine Berechtigung, den Discord Link zu ändern.")
                    }
                    return@launch
                }

                if (playerClan.members.size < DISCORD_LINK_REQUIRED_MEMBERS) {
                    player.sendText {
                        appendPrefix()
                        error("Dein Clan muss mindestens $DISCORD_LINK_REQUIRED_MEMBERS Mitglieder haben, um den Discord-Link ändern zu können.")
                    }
                    return@launch
                }

                if (discordFull.isNullOrEmpty()) {
                    return@launch
                }

                val split = discordFull.split(" ")
                if (split.isEmpty() || split.size > 1) {
                    player.sendText {
                        appendPrefix()
                        error("Du musst einen gültigen Discord-Invite Link angeben!")
                    }
                    return@launch
                }

                val discord = split[0]
                val discordInviteRegex = Regex(
                    "^(https?://)(www\\.)?(discord\\.gg|discord\\.com/invite)/[A-Za-z0-9]+/?$"
                )

                if (!discord.matches(discordInviteRegex)) {
                    player.sendText {
                        appendPrefix()
                        error("Du musst einen gültigen Discord-Invite Link angeben!")
                    }
                    return@launch
                }

                player.sendText {
                    appendPrefix()
                    success("Du hast den Discord Link auf ")
                    variableValue(discord)
                    success(" geändert.")
                }

                playerClan.discordInvite = discord
                clanService.saveClan(playerClan)
            }
        })
    }
}