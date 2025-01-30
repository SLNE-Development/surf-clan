package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.player
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

private const val CLAN_MAX_MEMBERS_DISBAND = 50

class ClanDisbandCommand(clanService: ClanService) : CommandAPICommand("disband") {
    init {
        withPermission("surf.clan.disband")

        stringArgument("confirm", true)

        executesPlayer(PlayerCommandExecutor { player, args ->
            val clan = player.findClan(clanService)

            if (clan == null) {
                player.sendMessage(Messages.notInClanComponent)

                return@PlayerCommandExecutor
            }

            if (!clan.hasPermission(player, ClanPermission.DISBAND)) {
                player.sendMessage(buildMessage {
                    append(
                        Component.text("Du hast keine Berechtigung, den Clan ", COLOR_ERROR)
                    )
                    append(clanComponent(clan))
                    append(Component.text(" aufzulösen.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            if (clan.members.size > CLAN_MAX_MEMBERS_DISBAND) {
                player.sendMessage(buildMessage {
                    append(Component.text("Du kannst den Clan ", COLOR_ERROR))
                    append(clanComponent(clan))
                    append(Component.text(" nicht auflösen, da er mehr als ", COLOR_ERROR))
                    append(Component.text(CLAN_MAX_MEMBERS_DISBAND.toString(), COLOR_VARIABLE))
                    append(
                        Component.text(
                            " Mitglieder hat. Wende dich bitte an den Support.",
                            COLOR_ERROR
                        )
                    )
                })

                return@PlayerCommandExecutor
            }

            val confirm = args.getOrDefaultUnchecked("confirm", "")
            if (confirm.isNotEmpty() && confirm == "confirm") {
                val clanDisbandedMessage = buildMessage {
                    append(Component.text("Der Clan ", COLOR_ERROR))
                    append(clanComponent(clan))
                    append(Component.text(" wurde aufgelöst.", COLOR_ERROR))
                }

                plugin.container.launch {
                    clanService.deleteClan(clan)

                    clan.members.forEach { member ->
                        member.player.sendMessage(clanDisbandedMessage)
                    }
                }
            }

            player.sendMessage(buildMessage {
                append(Component.text("Bist du sicher, dass du den Clan ", COLOR_INFO))
                append(clanComponent(clan))
                append(Component.text(" auflösen möchtest? Klicke ", COLOR_INFO))
                append(buildMessage(false) {
                    append(Component.text("hier", COLOR_VARIABLE, TextDecoration.BOLD))

                    hoverEvent(HoverEvent.showText(buildMessage(false) {
                        append(
                            Component.text(
                                "Klicke hier, um den Clan aufzulösen.",
                                NamedTextColor.GRAY
                            )
                        )
                        appendNewline()
                        appendNewline()

                        append(Component.text("Achtung: ", COLOR_ERROR, TextDecoration.BOLD))
                        append(Component.text("Alle Daten des Clans werden gelöscht ", COLOR_ERROR))
                        appendNewline()
                        append(
                            Component.text(
                                "und können nicht wiederhergestellt werden.",
                                COLOR_ERROR
                            )
                        )
                        appendNewline()
                        append(
                            Component.text(
                                "Auch der Support kann keine Daten wiederherstellen.",
                                COLOR_ERROR
                            )
                        )
                    }))

                    clickEvent(ClickEvent.suggestCommand("/clan disband confirm"))
                })
                append(Component.text(" um den Clan aufzulösen.", COLOR_INFO))
            })
        })
    }
}