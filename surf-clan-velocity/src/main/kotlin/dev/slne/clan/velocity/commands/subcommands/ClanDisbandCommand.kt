package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.clanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

private const val CLAN_MAX_MEMBERS_DISBAND = 50

class ClanDisbandCommand : CommandAPICommand("disband") {
    init {
        withPermission("surf.clan.disband")
        playerExecutor { player, args ->
            plugin.container.launch {
                val clan = player.findClan()

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                if (!clan.hasPermission(player, ClanPermission.DISBAND)) {
                    player.sendText {
                        appendPrefix()
                        error("Du hast keine Berechtigung, den Clan aufzulösen.")
                    }
                    return@launch
                }

                if (clan.members.size > CLAN_MAX_MEMBERS_DISBAND) {
                    player.sendText {
                        appendPrefix()
                        error("Du kannst den Clan nicht auflösen, da er mehr als $CLAN_MAX_MEMBERS_DISBAND Mitglieder hat.")
                    }

                    return@launch
                }

                player.sendText {
                    appendPrefix()
                    info("Bist du sicher, dass du den Clan ")
                    append(clanComponent(clan))
                    info(" auflösen möchtest? Klicke")
                    append(buildText {
                        append(Component.text("hier", Colors.VARIABLE_VALUE, TextDecoration.BOLD))

                        hoverEvent(HoverEvent.showText(buildText {
                            append(
                                Component.text(
                                    "Klicke hier, um den Clan aufzulösen.",
                                    NamedTextColor.GRAY
                                )
                            )
                            appendNewline()
                            appendNewline()

                            append(Component.text("Achtung: ", Colors.ERROR, TextDecoration.BOLD))
                            append(
                                Component.text(
                                    "Alle Daten des Clans werden gelöscht ",
                                    Colors.ERROR
                                )
                            )
                            appendNewline()
                            append(
                                Component.text(
                                    "und können nicht wiederhergestellt werden.",
                                    Colors.ERROR
                                )
                            )
                            appendNewline()
                            append(
                                Component.text(
                                    "Auch der Support kann keine Daten wiederherstellen.",
                                    Colors.ERROR
                                )
                            )
                        }))

                        clickEvent(ClickEvent.callback {
                            plugin.container.launch {
                                val clanDisbandedMessage = buildText {
                                    append(Component.text("Der Clan ", Colors.SUCCESS))
                                    append(clanComponent(clan))
                                    append(Component.text(" wurde aufgelöst.", Colors.SUCCESS))
                                }

                                clanService.deleteClan(clan)

                                clan.members.forEach { member ->
                                    member.playerOrNull?.sendMessage(clanDisbandedMessage)
                                }
                            }
                        })
                    })
                    info(" um den Clan aufzulösen.")
                }
            }
        }
    }
}