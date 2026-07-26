package dev.slne.clan.paper.commands.subcommands.member

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.optionalArgument
import dev.jorel.commandapi.kotlindsl.subcommand
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.paper.commands.arguments.ClanByClanTagArgument
import dev.slne.clan.paper.permission.ClanPermissions
import dev.slne.surf.api.core.command.args.awaitingOrNull
import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.Colors
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.pagination.Pagination
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components
import dev.slne.surf.core.api.common.SurfCoreApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import net.kyori.adventure.text.format.TextDecoration
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.ConcurrentHashMap

private val LAST_ACTIVE_FORMATTER = DateTimeFormatter.ofPattern("dd.MM.yyyy")

data class ClanMemberData(
    val memberName: String,
    val role: ClanMemberRole,
    val currentServer: String?,
    val lastActiveAt: OffsetDateTime,
    val isActive: Boolean
) {
    /** Online first, then active but offline, then inactive. */
    val activityRank get() = if (currentServer != null) 0 else if (isActive) 1 else 2
}

private suspend fun pagination(clan: ClanImpl): Pagination<ClanMemberData> {
    val clanInformationHover = Components.Clan.renderClanInformationHover(clan)

    return Pagination {
        title {
            primary("Mitglieder von ".toSmallCaps())
            append(clanInformationHover)
        }

        resultsPerPage = 10

        rowRenderer { member, _ ->
            listOf(
                buildText {
                    darkSpacer(">")
                    appendSpace()
                    if (member.currentServer != null) {
                        append {
                            darkSpacer("[")
                            success("🌎", TextDecoration.BOLD)
                            darkSpacer("]")
                            appendSpace()
                            white(member.memberName)
                            hoverEvent(buildText {
                                success("Online auf ")
                                variableValue(member.currentServer)
                            })
                        }
                    } else if (!member.isActive) {
                        append {
                            darkSpacer("[")
                            text("💤", Colors.GRAY)
                            darkSpacer("]")
                            appendSpace()
                            text(member.memberName, Colors.GRAY)
                            hoverEvent(buildText {
                                info("Zuletzt online am ")
                                variableValue(LAST_ACTIVE_FORMATTER.format(member.lastActiveAt))
                                appendNewline()
                                info("Zählt nicht als aktives Mitglied.")
                            })
                        }
                    } else {
                        white(member.memberName)
                    }
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
            Clan.byPlayer(player.uniqueId)
                ?: throw CommandAPI.failWithString("Du bist in keinem Clan.")
        }

        val data = ConcurrentHashMap.newKeySet<ClanMemberData>()
        supervisorScope {
            for (member in clan.members) {
                launch {
                    val name =
                        PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()

                    data.add(
                        ClanMemberData(
                            name,
                            member.role,
                            SurfCoreApi.getPlayer(member.uuid)?.currentServer?.displayName,
                            member.lastActiveAt,
                            member.isActive
                        )
                    )
                }
            }
        }

        val pagination = pagination(clan as ClanImpl)
        player.sendMessage(
            pagination.renderComponent(
                data.sortedWith(
                    compareBy<ClanMemberData> { it.activityRank }
                        .thenBy(nullsLast(reverseOrder())) { it.currentServer }
                        .thenBy { it.memberName }
                        .thenBy { it.role.name }
                )
            )
        )
    }
}