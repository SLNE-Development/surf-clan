package dev.slne.clan.minestom.command.subcommands

import dev.slne.minestom.lobby.api.command.commandapi.CommandAPICommand
import dev.slne.minestom.lobby.api.command.commandapi.dsl.anyExecutorSuspend
import dev.slne.minestom.lobby.api.command.commandapi.dsl.subcommand
import dev.slne.minestom.lobby.api.coroutine.minestomAsyncScope
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.clan.core.clan.CoreClanService
import dev.slne.surf.clan.core.client.command.clanListPagination
import dev.slne.surf.clan.core.client.command.handleClanListClick
import dev.slne.surf.clan.core.client.permission.ClanPermissions
import kotlinx.coroutines.launch
import net.kyori.adventure.text.event.ClickCallback
import net.kyori.adventure.text.event.ClickEvent
import net.minestom.server.entity.Player

private val pagination = clanListPagination { clanUuid ->
    ClickEvent.callback(ClickCallback.widen({ clicked ->
        minestomAsyncScope.launch {
            handleClanListClick(clicked, clanUuid)
        }
    }, Player::class.java))
}

fun CommandAPICommand.clanListCommand(): CommandAPICommand = withSubcommand(
    subcommand("list") {
        withPermission(ClanPermissions.CLAN_LIST_COMMAND)

        anyExecutorSuspend { executor, _ ->
            val clans = CoreClanService.fetchAllClansWithoutMembersSortByMemberCount()

            executor.sendText {
                appendNewline()
                append(pagination.renderComponent(clans))
            }
        }
    }
)
