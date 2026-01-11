package dev.slne.clan.velocity.commands.subcommands

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.core.clan.CoreClanService
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import dev.slne.surf.surfapi.velocity.api.command.executors.anyExecutorSuspend
import net.kyori.adventure.text.format.TextDecoration


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