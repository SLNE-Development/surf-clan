package dev.slne.clan.velocity.commands.subcommands.member

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.optionalArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.components.Components
import dev.slne.clan.velocity.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.velocity.permission.ClanPermissions
import dev.slne.surf.surfapi.core.api.command.args.awaitingOrNull
import dev.slne.surf.surfapi.core.api.font.toSmallCaps
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.pagination.Pagination
import dev.slne.surf.surfapi.core.api.service.PlayerLookupService
import dev.slne.surf.surfapi.velocity.api.command.executors.playerExecutorSuspend
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import java.util.concurrent.ConcurrentHashMap

data class ClanMemberData(val memberName: String, val role: ClanMemberRole)

private suspend fun pagination(clan: ClanImpl): Pagination<ClanMemberData> {
    val clanInformationHover = Components.Clan.renderClanInformationHover(clan)

    return Pagination {
        title {
            primary("Mitglieder von ".toSmallCaps())
            append(clanInformationHover)
        }

        resultsPerPage = 10

        rowRenderer { member, i ->
            listOf(
                buildText {
                    text(member.memberName, Colors.WHITE)
                    appendSpace()
                    spacer("(")
                    append(member.role)
                    spacer(")")
                }
            )
        }
    }
}

fun CommandAPICommand.clanMembersCommand() = subcommand("members") {
    withPermission(ClanPermissions.CLAN_VIEW_MEMBERS_COMMAND)

    optionalArgument(ClanByClanTagArgument("clan"))

    playerExecutorSuspend { player, args ->
        val clan = args.awaitingOrNull<Clan>("clan") ?: run {
            Clan.byPlayer(player.uniqueId) ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")
        }

        val data = ConcurrentHashMap.newKeySet<ClanMemberData>()
        supervisorScope {
            for (member in clan.members) {
                launch {
                    val name = PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()
                    data.add(ClanMemberData(name, member.role))
                }
            }
        }

        val pagination = pagination(clan as ClanImpl)
        player.sendMessage(pagination.renderComponent(data))
    }
}