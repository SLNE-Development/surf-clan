package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.greedyStringArgument
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.buildMessageAsync
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.ClanSettings.DISCORD_LINK_REQUIRED_MEMBERS
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.Component

class ClanSetDiscordCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("setdiscord") {
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
                val playerClan = player.findClan(clanService)
                val discordFull = args.getUnchecked<String>("discord")

                if (playerClan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                if (!playerClan.hasPermission(player, ClanPermission.DISCORD)) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du hast keine Berechtigung, den Discord Link zu ändern.",
                                Colors.ERROR
                            )
                        )
                    })

                    return@launch
                }

                if (playerClan.members.size < DISCORD_LINK_REQUIRED_MEMBERS) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du kannst den Discord-Link erst ab einer Mitgliederzahl von ",
                                Colors.ERROR
                            )
                        )
                        append(
                            Component.text(
                                "$DISCORD_LINK_REQUIRED_MEMBERS Mitgliedern",
                                Colors.VARIABLE_VALUE
                            )
                        )
                        append(Component.text(" ändern.", Colors.ERROR))
                    })

                    return@launch
                }

                if (discordFull.isNullOrEmpty()) {
                    return@launch
                }

                val split = discordFull.split(" ")
                if (split.isEmpty() || split.size > 1) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du musst einen gültigen Discord-Invite Link angeben!",
                                Colors.ERROR
                            )
                        )
                    })

                    return@launch
                }

                val discord = split[0]
                val discordInviteRegex = Regex(
                    "^(https?://)(www\\.)?(discord\\.gg|discord\\.com/invite)/[A-Za-z0-9]+/?$"
                )

                if (!discord.matches(discordInviteRegex)) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du musst einen gültigen Discord-Invite Link angeben!",
                                Colors.ERROR
                            )
                        )
                    })
                    return@launch
                }

                player.sendMessage(
                    buildMessageAsync {
                        append(Component.text("Du hast den Discord Link auf ", Colors.SUCCESS))
                        append(Component.text(discord, Colors.VARIABLE_VALUE))
                        append(Component.text(" geändert.", Colors.SUCCESS))
                    }
                )
                playerClan.discordInvite = discord
                clanService.saveClan(playerClan)
            }
        })
    }
}