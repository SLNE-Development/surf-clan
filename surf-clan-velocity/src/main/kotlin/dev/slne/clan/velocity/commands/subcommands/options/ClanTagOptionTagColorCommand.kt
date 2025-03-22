package dev.slne.clan.velocity.commands.subcommands.options

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.getValue
import dev.jorel.commandapi.kotlindsl.multiLiteralArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.tag.ClanTagColor
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.tag.ClanTagColors
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class ClanTagOptionTagColorCommand(
    clanService: ClanService
) : CommandAPICommand("tagcolor") {
    init {
        withPermission("surf.clan.options.tagcolor")

        multiLiteralArgument(
            "color",
            literals = ClanTagColors.colors.map { it.clanTagColor.name }.toTypedArray()
        )

        playerExecutor { player, args ->
            val color: String by args
            val tagColor = runCatching { ClanTagColor.valueOf(color) }.getOrNull()
                ?: run {
                    player.sendText {
                        appendPrefix()
                        error("Die angegebene Farbe existiert nicht.")
                    }
                    return@playerExecutor
                }

            val clan = player.findClan(clanService)

            if (clan == null) {
                player.sendMessage(Messages.notInClanComponent)
                return@playerExecutor
            }

            plugin.container.launch {
                if (!clan.hasPermission(player, ClanPermission.OPTIONS_TAG_COLOR)) {
                    player.sendText {
                        appendPrefix()
                        error("Du hast keine Berechtigung, die Farbe des Clan-Tags zu ändern.")
                    }
                    return@launch
                }

                clan.clanTagColor = tagColor
                clanService.saveClan(clan)

                player.sendText {
                    appendPrefix()

                    success("Die Farbe des Clan-Tags wurde erfolgreich geändert und wird in einigen Sekunden Netzwerkweit aktualisiert.")
                }
            }
        }
    }
}