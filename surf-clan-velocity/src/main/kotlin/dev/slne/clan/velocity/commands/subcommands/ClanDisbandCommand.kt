package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.proxyPlayerOrNull
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextDecoration

private const val CLAN_MAX_MEMBERS_DISBAND = 50

object ClanDisbandCommand : CommandAPICommand("disband") {
    init {
        withPermission("surf.clan.disband")

        stringArgument("confirm", true)

        playerExecutor { player, args ->
            plugin.container.launch {
                val clan = player.findClan()

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                if (!clan.hasPermission(player, ClanPermission.DISBAND)) {
                    player.sendText {
                        error("Du hast keine Berechtigung, den Clan ")
                        append(clanComponent(clan))
                        error(" aufzulösen.")
                    }

                    return@launch
                }

                if (clan.members.size > CLAN_MAX_MEMBERS_DISBAND) {
                    player.sendText {
                        error("Du kannst den Clan ")
                        append(clanComponent(clan))
                        error(" nicht auflösen, da er mehr als ")
                        variableValue(CLAN_MAX_MEMBERS_DISBAND.toString())
                        error(" Mitglieder hat. Wende dich bitte an den Support.")
                    }

                    return@launch
                }

                val confirm = args.getOrDefaultUnchecked("confirm", "")
                if (confirm.isNotEmpty() && confirm == "confirm") {
                    val clanDisbandedMessage = buildText {
                        success("Der Clan ")
                        append(clanComponent(clan))
                        success(" wurde aufgelöst.")
                    }

                    ClanService.deleteClan(clan)

                    clan.members.forEach { member ->
                        member.proxyPlayerOrNull?.sendMessage(clanDisbandedMessage)
                    }

                    return@launch
                }

                player.sendText {
                    info("Bist du sicher, dass du den Clan ")
                    append(clanComponent(clan))
                    info(" auflösen möchtest? Klicke ")

                    append(buildText {
                        append(Component.text("hier", Colors.VARIABLE_VALUE, TextDecoration.BOLD))

                        hoverEvent(HoverEvent.showText(buildText {
                            spacer("Klicke hier, um den Clan aufzulösen")
                            appendNewline()
                            appendNewline()

                            append(Component.text("Achtung: ", Colors.ERROR, TextDecoration.BOLD))
                            error("Alle Daten des Clans werden gelöscht ")
                            appendNewline()
                            error("und können nicht wiederhergestellt werden.")
                            appendNewline()
                            error("Auch der Support kann keine Daten wiederherstellen.")
                        }))

                        clickEvent(ClickEvent.suggestCommand("/clan disband confirm"))
                    })
                    
                    info(" um den Clan aufzulösen.")
                }
            }
        }
    }
}