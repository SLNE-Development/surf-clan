package dev.slne.clan.velocity.commands.subcommands.member

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.service.NameCacheService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

private const val MEMBERS_PER_PAGE = 1 //TODO: Change to 10

class ClanMembersCommand(
    clanService: ClanService,
    nameCacheService: NameCacheService
) : CommandAPICommand("members") {
    init {
        withPermission("surf.clan.members")

        integerArgument("page", 1, optional = true)

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val clan = player.findClan(clanService)
                var page = args.getOrDefaultUnchecked("page", 1)

                val chunkedMembers = clan?.members?.chunked(MEMBERS_PER_PAGE) ?: emptyList()
                val totalPages = chunkedMembers.size

                page = if (page < 1) 1 else page
                page = if (page > totalPages) totalPages else page

                val pageMembers = chunkedMembers.getOrNull(page - 1) ?: emptyList()

                if (clan == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                val clanInfoMessage = buildMessageAsync(false) {
                    append(Component.text("Mitglieder von ", COLOR_INFO))
                    append(clanComponent(clan, nameCacheService))
                    append(Component.text(" (Seite $page/$totalPages)", COLOR_INFO))
                    appendNewline()

                    pageMembers.forEach { member ->
                        append(Component.text(" - ", COLOR_INFO))
                        append(Component.text(member.uuid.toString()))
                        append(Component.text(" (", COLOR_INFO))
                        append(Component.text(member.role.toString(), COLOR_VARIABLE))
                        append(Component.text(")", COLOR_INFO))
                        appendNewline()
                    }

                    val firstPageComponent = renderPageSwapComponent(buildMessage(false) {
                        append(Component.text("[", NamedTextColor.GRAY))
                        append(Component.text("<<", COLOR_ERROR))
                        append(Component.text("]", NamedTextColor.GRAY))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
                            append(Component.text("Zur ersten Seite", COLOR_INFO))
                        }))

                        clickEvent(ClickEvent.runCommand("/clan members 1"))
                    }, page) { it > 1 }

                    val previousPageComponent = renderPageSwapComponent(buildMessage(false) {
                        append(Component.text("[", NamedTextColor.GRAY))
                        append(Component.text("<", COLOR_ERROR))
                        append(Component.text("]", NamedTextColor.GRAY))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
                            append(Component.text("Zurück zur Seite ${page - 1}", COLOR_INFO))
                        }))

                        clickEvent(ClickEvent.runCommand("/clan members ${page - 1}"))
                    }, page) { it > 1 }

                    val nextPageComponent = renderPageSwapComponent(buildMessage(false) {
                        append(Component.text("[", NamedTextColor.GRAY))
                        append(Component.text(">", COLOR_SUCCESS))
                        append(Component.text("]", NamedTextColor.GRAY))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
                            append(Component.text("Weiter zur Seite ${page + 1}", COLOR_INFO))
                        }))

                        clickEvent(ClickEvent.runCommand("/clan members ${page + 1}"))
                    }, page) { it < totalPages }

                    val lastPageComponent = renderPageSwapComponent(buildMessage(false) {
                        append(Component.text("[", NamedTextColor.GRAY))
                        append(Component.text(">>", COLOR_SUCCESS))
                        append(Component.text("]", NamedTextColor.GRAY))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
                            append(Component.text("Zur letzten Seite", COLOR_INFO))
                        }))

                        clickEvent(ClickEvent.runCommand("/clan members $totalPages"))
                    }, page) { it < totalPages }

                    val pagination = buildMessage(false) {
                        append(firstPageComponent)
                        appendSpace()
                        append(previousPageComponent)
                        appendSpace()
                        append(Component.text("Seite $page/$totalPages", NamedTextColor.GRAY))
                        appendSpace()
                        append(nextPageComponent)
                        appendSpace()
                        append(lastPageComponent)
                    }

                    append(pagination)
                }

                player.sendMessage(clanInfoMessage)
            }
        })
    }

    private fun renderPageSwapComponent(
        message: Component,
        currentPage: Int,
        shouldRender: (Int) -> Boolean
    ): Component {
        return if (shouldRender(currentPage)) {
            message
        } else {
            val pcs = PlainTextComponentSerializer.plainText()
            val plainText = pcs.serialize(message)

            Component.text(plainText).color(NamedTextColor.GRAY)
        }
    }
}