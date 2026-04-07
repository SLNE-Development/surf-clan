package dev.slne.clan.paper.commands.subcommands

import com.github.shynixn.mccoroutine.folia.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.clan.paper.plugin
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.clan.CoreClanService
import dev.slne.surf.clan.core.client.components.Components
import dev.slne.surf.surfapi.bukkit.api.command.executors.anyExecutorSuspend
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.entity.Player


private val pagination = Pagination<Clan> {
    title { primary("Clans".toSmallCaps(), TextDecoration.BOLD) }

    rowRenderer { clan, _ ->
        listOf(
            buildText {
                spacer(">")
                appendSpace()
                variableValue(clan.name)
                appendSpace()
                info("(${clan.tag})")

                val clanUuid = clan.uuid
                clickEvent(ClickEvent.callback(ClickCallback.widen({ clicked ->
                    plugin.launch {
                        val clan = Clan.byUuid(clanUuid)
                        if (clan == null) {
                            clicked.sendText {
                                appendErrorPrefix()
                                error("Der Clan konnte nicht gefunden werden.")
                            }
                        } else {
                            clicked.sendMessage(Components.Clan.renderClanInformation(clan as ClanImpl))
                        }
                    }
                }, Player::class.java)))
            }
        )
    }
}

fun CommandAPICommand.clanListCommand() = subcommand("list") {
    withPermission(ClanPermissions.CLAN_LIST_COMMAND)

    anyExecutorSuspend { executor, _ ->
        val clans = CoreClanService.fetchAllClansWithoutMembersSortByMemberCount()

        executor.sendText {
            appendNewline()
            append(pagination.renderComponent(clans))
        }
    }
}