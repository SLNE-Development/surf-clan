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
import dev.slne.surf.api.core.messages.builder.SurfComponentBuilder
import dev.slne.surf.api.core.messages.pagination.Pagination
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.api.core.util.dateTimeFormatter
import dev.slne.surf.api.paper.command.executors.playerExecutorSuspend
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components
import dev.slne.surf.core.api.common.SurfCoreApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import net.kyori.adventure.text.format.TextDecoration
import java.time.Duration
import java.time.OffsetDateTime
import java.util.concurrent.ConcurrentHashMap

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

/**
 * Renders how long ago [timestamp] was, in the coarsest unit that still carries information:
 * "Gerade eben", "Vor 5 Minuten", "Vor 3 Stunden", "Vor 12 Tagen".
 *
 * A [timestamp] that is not in the past — clock skew between servers, or the created_at fallback
 * landing a moment ahead — falls through to "Gerade eben" rather than rendering a negative amount.
 */
private fun formatTimeAgo(timestamp: OffsetDateTime, now: OffsetDateTime): String {
    val elapsed = Duration.between(timestamp, now)

    val days = elapsed.toDays()
    if (days > 0) return "Vor $days ${if (days == 1L) "Tag" else "Tagen"}"

    val hours = elapsed.toHours()
    if (hours > 0) return "Vor $hours ${if (hours == 1L) "Stunde" else "Stunden"}"

    val minutes = elapsed.toMinutes()
    if (minutes > 0) return "Vor $minutes ${if (minutes == 1L) "Minute" else "Minuten"}"

    return "Gerade eben"
}

/** Appends "Zuletzt online: Vor 3 Stunden (10.02.2026 19:30 Uhr)" to a hover. */
private fun SurfComponentBuilder.appendLastSeen(
    lastActiveAt: OffsetDateTime,
    now: OffsetDateTime
) {
    info("Zuletzt online: ")
    variableValue(formatTimeAgo(lastActiveAt, now))
    spacer(" (")
    variableValue("${dateTimeFormatter.format(lastActiveAt)} Uhr")
    spacer(")")
}

private suspend fun pagination(clan: ClanImpl): Pagination<ClanMemberData> {
    val clanInformationHover = Components.Clan.renderClanInformationHover(clan)
    val now = OffsetDateTime.now()

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
                    } else {
                        append {
                            if (member.isActive) {
                                white(member.memberName)
                            } else {
                                darkSpacer("[")
                                text("💤", Colors.GRAY)
                                darkSpacer("]")
                                appendSpace()
                                text(member.memberName, Colors.GRAY)
                            }
                            hoverEvent(buildText {
                                appendLastSeen(member.lastActiveAt, now)
                            })
                        }
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