package dev.slne.clan.paper.commands.subcommands.member

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.kotlindsl.commandAPICommand
import dev.slne.clan.api.clan.ClanService
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.core.api.common.SurfCoreApi
import dev.slne.surf.surfapi.bukkit.api.command.executors.playerExecutorSuspend
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import net.kyori.adventure.text.format.TextDecoration

private data class PlayerServerData(
    val playerName: String,
    val playerRole: ClanMemberRole,
    val serverName: String
)

private val pagination = Pagination<PlayerServerData> {
    title { primary("Clanmitglieder online".toSmallCaps(), TextDecoration.BOLD) }

    resultsPerPage = 10

    rowRenderer { item, _ ->
        listOf(
            buildText {
                variableValue(item.playerName)

                appendSpace()
                spacer("(")
                append(item.playerRole.displayName)
                spacer(")")

                appendSpace()
                spacer("(")
                info(item.serverName)
                spacer(")")
            }
        )
    }
}

fun whoIsOnlineCommand() = commandAPICommand("whoIsOnline") {
    playerExecutorSuspend { executor, _ ->
        val clan = ClanService.findClanByPlayer(executor.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")

        val clanMembers = clan.members
            .filter { it.uuid != executor.uniqueId }
            .associateBy { it.uuid }

        val data = SurfCoreApi.getCommonServers().flatMap { server ->
            server.getPlayers()
                .filter { surfPlayer -> surfPlayer.uuid in clanMembers.keys }
                .map { surfPlayer ->
                    val member = clanMembers[surfPlayer.uuid]!!

                    PlayerServerData(
                        playerName = surfPlayer.username,
                        playerRole = member.role,
                        serverName = server.displayName
                    )
                }
        }

        executor.sendText {
            appendNewline()
            append(pagination.renderComponent(data))
        }
    }
}