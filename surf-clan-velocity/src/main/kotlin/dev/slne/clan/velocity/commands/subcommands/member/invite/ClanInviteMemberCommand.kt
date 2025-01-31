package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.PlayerArgument
import dev.slne.clan.velocity.commands.arguments.playerArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor

class ClanInviteMemberCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("invite") {
    init {
        withPermission("surf.clan.invite")

        withSubcommands(ClanInviteAcceptCommand(clanService, clanPlayerService))
        withSubcommands(ClanInviteDenyCommand(clanService, clanPlayerService))

        playerArgument()

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val invitedPlayer = PlayerArgument.player(args)

                if (invitedPlayer == null) {
                    player.sendMessage(buildMessage {
                        append(Component.text("Der Spieler ", COLOR_ERROR))
                        append(Component.text(args[0] as String, COLOR_VARIABLE))
                        append(Component.text(" ist nicht online.", COLOR_ERROR))
                    })

                    return@launch
                }

                val invitedPlayerClan = invitedPlayer.findClan(clanService)

                if (invitedPlayerClan != null) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Der Spieler ", COLOR_ERROR))
                        append(invitedPlayer.realName())
                        append(Component.text(" ist bereits im Clan ", COLOR_ERROR))
                        append(clanComponent(invitedPlayerClan, clanPlayerService))
                        append(Component.text(".", COLOR_ERROR))
                    })

                    return@launch
                }

                val playerClan = player.findClan(clanService)

                if (playerClan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                if (!playerClan.hasPermission(player, ClanPermission.INVITE)) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text(
                                "Du hast keine Berechtigung, Spieler in den Clan ",
                                COLOR_ERROR
                            )
                        )
                        append(clanComponent(playerClan, clanPlayerService))
                        append(Component.text(" einzuladen.", COLOR_ERROR))
                    })

                    return@launch
                }

                val clanPlayer = clanPlayerService.findClanPlayerByUuid(invitedPlayer.uniqueId)
                if (clanPlayer == null) {
                    player.sendMessage(
                        Component.text(
                            "Ein Fehler ist aufgetreten. Bitte versuche es erneut.",
                            NamedTextColor.RED
                        )
                    )

                    return@launch
                }

                if (!clanPlayer.acceptsClanInvites) {
                    player.sendMessage(buildMessage {
                        append(Component.text("Der Spieler ", COLOR_ERROR))
                        append(invitedPlayer.realName())
                        append(Component.text(" nimmt keine Einladungen an.", COLOR_ERROR))
                    })

                    return@launch
                }

                val inviteResult = playerClan.invite(invitedPlayer.uniqueId, player.uniqueId)

                if (inviteResult) {
                    clanService.saveClan(playerClan)

                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Du hast ", COLOR_SUCCESS))
                        append(invitedPlayer.realName())
                        append(Component.text(" in den Clan ", COLOR_SUCCESS))
                        append(clanComponent(playerClan, clanPlayerService))
                        append(Component.text(" eingeladen.", COLOR_SUCCESS))
                    })

                    invitedPlayer.sendMessage(buildMessageAsync {
                        append(Component.text("Du wurdest von ", COLOR_INFO))
                        append(player.realName())
                        append(Component.text(" in den Clan ", COLOR_INFO))
                        append(clanComponent(playerClan, clanPlayerService))
                        append(Component.text(" eingeladen. ", COLOR_INFO))

                        val acceptComponent = buildMessage(false) {
                            append(Component.text("[Annehmen]", COLOR_SUCCESS))
                            hoverEvent(
                                HoverEvent.showText(
                                    Component.text(
                                        "Klicke hier, um die Einladung anzunehmen.",
                                        NamedTextColor.GREEN
                                    )
                                )
                            )
                            clickEvent(ClickEvent.runCommand("/clan invite ${player.username} accept ${playerClan.name}"))
                        }

                        append(acceptComponent)
                        appendSpace()

                        val denyComponent = buildMessage(false) {
                            append(Component.text("[Ablehnen]", COLOR_ERROR))
                            hoverEvent(
                                HoverEvent.showText(
                                    Component.text(
                                        "Klicke hier, um die Einladung abzulehnen.",
                                        NamedTextColor.RED
                                    )
                                )
                            )
                            clickEvent(ClickEvent.runCommand("/clan invite ${player.username} deny ${playerClan.name}"))
                        }

                        append(denyComponent)
                    })
                } else {
                    player.sendMessage(buildMessage {
                        append(Component.text("Der Spieler ", COLOR_ERROR))
                        append(invitedPlayer.realName())
                        append(Component.text(" wurde bereits eingeladen.", COLOR_ERROR))
                    })
                }
            }
        })
    }
}