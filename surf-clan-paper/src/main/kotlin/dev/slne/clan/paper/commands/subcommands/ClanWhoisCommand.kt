package dev.slne.clan.paper.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaitingOrNull
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.core.api.common.player.SurfPlayer
import dev.slne.surf.core.api.paper.command.argument.surfOfflinePlayerArgument
import net.kyori.adventure.text.event.ClickEvent

fun CommandAPICommand.clanWhoisCommand() = subcommand("whois") {
    withPermission(ClanPermissions.CLAN_WHOIS_COMMAND)
    surfOfflinePlayerArgument("target")
    playerExecutorSuspend { player, args ->
        val target = args.awaitingOrNull<SurfPlayer?>("target")

        if (target == null) {
            player.sendText {
                appendErrorPrefix()
                error("Der Spieler wurde nicht gefunden!")
            }
            return@playerExecutorSuspend
        }

        val targetName = target.lastKnownName ?: target.uuid.toString()
        val clan = Clan.byPlayer(target.uuid)
        if (clan == null) {
            player.sendText {
                appendInfoPrefix()
                variableValue(targetName)
                info(" ist in keinem Clan.")
            }
        } else {
            player.sendText {
                appendInfoPrefix()
                variableValue(targetName)
                info(" ist im Clan ")
                append {
                    variableValue(clan.name)
                    hoverEvent(buildText {
                        info("Klicke, um Informationen über den Clan anzuzeigen.")
                    })
                    clickEvent(ClickEvent.runCommand("/clan info ${clan.tag}"))
                }
                info(".")
            }
        }
    }
}