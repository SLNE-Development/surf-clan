package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.slne.clan.api.Clan
import dev.slne.clan.core.service.clanService
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import net.kyori.adventure.text.format.TextDecoration

class ClanListCommand : CommandAPICommand("list") {
    init {
        withPermission("surf.clan.list")
        anyExecutor { executor, _ ->
            val pagination = Pagination<Clan> {
                title { primary("Clans".toSmallCaps(), TextDecoration.BOLD) }
                rowRenderer { clan, _ ->
                    listOf(
                        buildText {
                            spacer(">")
                            appendSpace()
                            variableValue(clan.name)
                            appendSpace()
                            info("(${clan.tag})")
                        }
                    )
                }
            }

            executor.sendText {
                appendNewline()
                append(pagination.renderComponent(clanService.clans.sortedByDescending { it.members.size }))
            }
        }
    }
}