package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.buildMessageAsync
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

private const val CLAN_MAX_MEMBERS_DISBAND = 50

class ClanDisbandCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("disband") {
    init {
        withPermission("surf.clan.disband")

        stringArgument("confirm", true)

        playerExecutor { player, args ->
            plugin.container.launch {
                val clan = player.findClan(clanService)

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                if (!clan.hasPermission(player, ClanPermission.DISBAND)) {
                    player.sendMessage(buildMessageAsync {
                        append(
                            Component.text("Du hast keine Berechtigung, den Clan ", Colors.ERROR)
                        )
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(" aufzulösen.", Colors.ERROR))
                    })

                    return@launch
                }

                if (clan.members.size > CLAN_MAX_MEMBERS_DISBAND) {
                    player.sendMessage(buildMessageAsync {
                        append(Component.text("Du kannst den Clan ", Colors.ERROR))
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(" nicht auflösen, da er mehr als ", Colors.ERROR))
                        append(
                            Component.text(
                                CLAN_MAX_MEMBERS_DISBAND.toString(),
                                Colors.VARIABLE_VALUE
                            )
                        )
                        append(
                            Component.text(
                                " Mitglieder hat. Wende dich bitte an den Support.",
                                Colors.ERROR
                            )
                        )
                    })

                    return@launch
                }

                val confirm = args.getOrDefaultUnchecked("confirm", "")
                if (confirm.isNotEmpty() && confirm == "confirm") {
                    val clanDisbandedMessage = buildMessageAsync {
                        append(Component.text("Der Clan ", Colors.SUCCESS))
                        append(clanComponent(clan, clanPlayerService))
                        append(Component.text(" wurde aufgelöst.", Colors.SUCCESS))
                    }

                    clanService.deleteClan(clan)

                    clan.members.forEach { member ->
                        member.playerOrNull?.sendMessage(clanDisbandedMessage)
                    }

                    return@launch
                }

                player.sendMessage(buildMessageAsync {
                    append(Component.text("Bist du sicher, dass du den Clan ", Colors.INFO))
                    append(clanComponent(clan, clanPlayerService))
                    append(Component.text(" auflösen möchtest? Klicke ", Colors.INFO))
                    append(buildMessage(false) {
                        append(Component.text("hier", Colors.VARIABLE_VALUE, TextDecoration.BOLD))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
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

                        clickEvent(ClickEvent.suggestCommand("/clan disband confirm"))
                    })
                    append(Component.text(" um den Clan aufzulösen.", Colors.INFO))
                })
            }
        }
    }
}