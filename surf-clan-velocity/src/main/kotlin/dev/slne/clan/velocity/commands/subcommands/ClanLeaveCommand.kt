package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.clanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.player
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class ClanLeaveCommand : CommandAPICommand("leave") {
    init {
        withPermission("surf.clan.leave")
        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val clan = player.findClan()

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                player.sendText {
                    info("Möchtest du den Clan ")
                    append(clanComponent(clan))
                    info(" wirklich verlassen? Klicke ")
                    append(buildText {
                        append(Component.text("hier", Colors.VARIABLE_VALUE, TextDecoration.BOLD))
                        hoverEvent(HoverEvent.showText(buildText {
                            append(
                                Component.text(
                                    "Klicke hier, um zu bestätigen.",
                                    NamedTextColor.GRAY
                                )
                            )
                            appendNewline()
                            appendNewline()
                            appendNewline()

                            append(
                                Component.text(
                                    "Achtung: ",
                                    NamedTextColor.RED,
                                    TextDecoration.BOLD
                                )
                            )
                            append(
                                Component.text(
                                    "Du kannst den Vorgang nicht rückgängig machen.",
                                    NamedTextColor.RED
                                )
                            )
                            appendNewline()
                            appendNewline()

                            append(
                                Component.text(
                                    "Wenn du den Clan verlässt, verlierst du alle Rechte ",
                                    NamedTextColor.RED
                                )
                            )
                            appendNewline()
                            append(
                                Component.text(
                                    "und kannst nicht mehr selbstständig in den Clan zurückkehren.",
                                    NamedTextColor.RED
                                )
                            )
                            appendNewline()
                            appendNewline()

                            append(
                                Component.text(
                                    "Wenn du der Besitzer des Clans bist ",
                                    NamedTextColor.RED,
                                    TextDecoration.BOLD
                                )
                            )
                            append(
                                Component.text(
                                    "und ihn verlässt, wird der Clan aufgelöst.",
                                    NamedTextColor.RED,
                                    TextDecoration.BOLD
                                )
                            )
                        }))
                        clickEvent(ClickEvent.callback {
                            plugin.container.launch {
                                val clanDisbandedMessage = buildText {
                                    append(Component.text("Der Clan ", Colors.INFO))
                                    append(clanComponent(clan))
                                    append(
                                        Component.text(
                                            " wurde aufgelöst, da der Anführer ",
                                            Colors.INFO
                                        )
                                    )
                                    append(player.realName())
                                    append(Component.text(" den Clan verlassen hat.", Colors.INFO))
                                }

                                if (clan.createdBy == player.uniqueId) {
                                    clanService.deleteClan(clan)

                                    clan.members.forEach { member ->
                                        member.player.sendMessage(clanDisbandedMessage)
                                    }
                                } else {
                                    val clanMember =
                                        clan.members.find { it.uuid == player.uniqueId }

                                    if (clanMember == null) {
                                        player.sendMessage(Messages.notInClanComponent)

                                        return@launch
                                    }

                                    clan.removeMember(clanMember)
                                    clanService.saveClan(clan)

                                    clan.members.forEach { member ->
                                        member.playerOrNull?.sendText {
                                            info("Der Spieler ")
                                            variableValue(player.username)
                                            info(" hat den Clan verlassen.")
                                        }
                                    }
                                }
                            }
                        })
                    })
                    info(" um zu bestätigen.")
                }
            }
        })
    }
}