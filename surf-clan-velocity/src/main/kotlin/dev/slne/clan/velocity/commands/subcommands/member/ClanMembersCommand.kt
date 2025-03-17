package dev.slne.clan.velocity.commands.subcommands.member

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.jorel.commandapi.kotlindsl.stringArgument
import dev.slne.clan.core.Messages
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.buildMessageAsync
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.CLAN_COMPONENT_BAR_COLOR
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.subcommands.includeClanTagSuggestions
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import net.kyori.adventure.text.format.TextDecoration
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer

private const val MEMBERS_PER_PAGE = 10

class ClanMembersCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService
) : CommandAPICommand("members") {
    init {
        withPermission("surf.clan.members")

        stringArgument("clan", optional = true) {
            includeClanTagSuggestions(clanService)
        }
        integerArgument("page", 1, optional = true)

        playerExecutor { player, args ->
            plugin.container.launch {
                var clan = player.findClan(clanService)
                val clanTag = args.getUnchecked<String>("clan")
                var page = args.getOrDefaultUnchecked("page", 1)

                if (clanTag != null) {
                    clan = clanService.findClanByTag(clanTag)
                }

                if (clan == null) {
                    player.sendMessage(
                        if (clanTag == null) Messages.notInClanComponent else Messages.unknownClanComponent(
                            clanTag
                        )
                    )

                    return@launch
                }

                val chunkedMembers = clan.members
                    .sortedBy { it.role.ordinal }
                    .reversed()
                    .chunked(MEMBERS_PER_PAGE)
                val totalPages = chunkedMembers.size

                page = if (page < 1) 1 else page
                page = if (page > totalPages) totalPages else page

                val pageMembers = chunkedMembers.getOrNull(page - 1) ?: emptyList()

                val clanInfoMessage = buildMessageAsync(false) {
                    appendNewline()
                    append(Component.text("ᴍɪᴛɢʟɪᴇᴅᴇʀ ᴠᴏɴ ", CLAN_COMPONENT_BAR_COLOR))
                    append(clanComponent(clan, clanPlayerService))
                    appendNewline()

                    pageMembers.forEach { member ->
                        val memberName =
                            clanPlayerService.findClanPlayerByUuid(member.uuid)?.username
                                ?: member.uuid.toString()

                        append(buildMessage(false) {
                            append(
                                Component.text(
                                    "| ",
                                    CLAN_COMPONENT_BAR_COLOR,
                                    TextDecoration.BOLD
                                )
                            )
                            append(Component.text(memberName, NamedTextColor.WHITE))
                            append(Component.text(" (", NamedTextColor.GRAY))
                            append(Component.text(member.role.toString(), CLAN_COMPONENT_BAR_COLOR))
                            append(Component.text(")", NamedTextColor.GRAY))
                        })

                        appendNewline()
                    }

                    val firstPageComponent = renderPageSwapComponent(buildMessage(false) {
                        append(Component.text("[", NamedTextColor.GRAY))
                        append(Component.text("<<", Colors.ERROR))
                        append(Component.text("]", NamedTextColor.GRAY))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
                            append(Component.text("Zur ersten Seite", Colors.INFO))
                        }))

                        clickEvent(ClickEvent.runCommand("/clan members ${clan.tag} 1"))
                    }, page) { it > 1 }

                    val previousPageComponent = renderPageSwapComponent(buildMessage(false) {
                        append(Component.text("[", NamedTextColor.GRAY))
                        append(Component.text("<", Colors.ERROR))
                        append(Component.text("]", NamedTextColor.GRAY))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
                            append(Component.text("Zurück zur Seite ${page - 1}", Colors.INFO))
                        }))

                        clickEvent(ClickEvent.runCommand("/clan members ${clan.tag} ${page - 1}"))
                    }, page) { it > 1 }

                    val nextPageComponent = renderPageSwapComponent(buildMessage(false) {
                        append(Component.text("[", NamedTextColor.GRAY))
                        append(Component.text(">", Colors.SUCCESS))
                        append(Component.text("]", NamedTextColor.GRAY))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
                            append(Component.text("Weiter zur Seite ${page + 1}", Colors.INFO))
                        }))

                        clickEvent(ClickEvent.runCommand("/clan members ${clan.tag} ${page + 1}"))
                    }, page) { it < totalPages }

                    val lastPageComponent = renderPageSwapComponent(buildMessage(false) {
                        append(Component.text("[", NamedTextColor.GRAY))
                        append(Component.text(">>", Colors.SUCCESS))
                        append(Component.text("]", NamedTextColor.GRAY))

                        hoverEvent(HoverEvent.showText(buildMessage(false) {
                            append(Component.text("Zur letzten Seite", Colors.INFO))
                        }))

                        clickEvent(ClickEvent.runCommand("/clan members ${clan.tag} $totalPages"))
                    }, page) { it < totalPages }

                    val pagination = buildMessage(false) {
                        append(firstPageComponent)
                        appendSpace()
                        append(previousPageComponent)
                        appendSpace()
                        append(
                            Component.text(
                                "sᴇɪᴛᴇ $page/$totalPages",
                                NamedTextColor.GRAY
                            )
                        )
                        appendSpace()
                        append(nextPageComponent)
                        appendSpace()
                        append(lastPageComponent)
                    }

                    appendNewline()
                    append(pagination)
                }

                player.sendMessage(clanInfoMessage)
            }
        }
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