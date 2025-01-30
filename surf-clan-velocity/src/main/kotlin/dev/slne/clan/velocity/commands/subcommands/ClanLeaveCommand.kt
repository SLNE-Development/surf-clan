package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.service.NameCacheService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.player
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.extensions.realName
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration

class ClanLeaveCommand(
    clanService: ClanService,
    nameCacheService: NameCacheService
) : CommandAPICommand("leave") {
    init {
        withPermission("surf.clan.leave")

        stringArgument("confirm", true)

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val clan = player.findClan(clanService)

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                val confirm = args.getOrDefaultUnchecked("confirm", "")
                if (confirm.isNotEmpty() && confirm == "confirm") {
                    val clanDisbandedMessage = buildMessageAsync {
                        append(Component.text("Der Clan ", COLOR_INFO))
                        append(clanComponent(clan, nameCacheService))
                        append(Component.text(" wurde aufgelöst, da der Anführer ", COLOR_INFO))
                        append(player.realName())
                        append(Component.text(" den Clan verlassen hat.", COLOR_INFO))
                    }

                    if (clan.createdBy == player.uniqueId) {
                        clanService.deleteClan(clan)

                        clan.members.forEach { member ->
                            member.player.sendMessage(clanDisbandedMessage)
                        }
                    } else {
                        val clanMember = clan.members.find { it.player.uniqueId == player.uniqueId }

                        if (clanMember == null) {
                            player.sendMessage(Messages.notInClanComponent)

                            return@launch
                        }

                        clan.removeMember(clanMember)
                        clanService.saveClan(clan)

                        clan.members.forEach { member ->
                            member.playerOrNull?.sendMessage(buildMessage {
                                append(Component.text("Der Spieler ", COLOR_INFO))
                                append(player.realName())
                                append(Component.text(" hat den Clan verlassen.", COLOR_INFO))
                            })
                        }

                        player.sendMessage(buildMessageAsync {
                            append(Component.text("Du hast den Clan ", COLOR_SUCCESS))
                            append(clanComponent(clan, nameCacheService))
                            append(Component.text(" verlassen.", COLOR_SUCCESS))
                        })
                    }

                    return@launch
                }

                player.sendMessage(buildMessageAsync {
                    append(Component.text("Möchtest du den Clan ", COLOR_INFO))
                    append(clanComponent(clan, nameCacheService))
                    append(Component.text(" wirklich verlassen? Klicke ", COLOR_INFO))
                    append(buildMessage(false) {
                        append(Component.text("hier", COLOR_VARIABLE, TextDecoration.BOLD))
                        hoverEvent(HoverEvent.showText(buildMessage(false) {
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
                        clickEvent(ClickEvent.suggestCommand("/clan leave confirm"))
                    })
                    append(Component.text(" um zu bestätigen.", COLOR_INFO))
                })
            }
        })
    }
}