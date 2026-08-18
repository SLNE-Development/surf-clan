package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.surf.api.core.font.toSmallCaps
import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.api.core.messages.adventure.sendText
import dev.slne.surf.api.core.messages.pagination.Pagination
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components
import net.kyori.adventure.audience.Audience
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.format.TextDecoration
import java.util.*

/**
 * Lists the clans of the network, one page at a time.
 *
 * [clanClickEvent] supplies what clicking a row does, given the clan's uuid.
 */
fun clanListPagination(clanClickEvent: (clanUuid: UUID) -> ClickEvent<*>) = Pagination<Clan> {
    title { primary("Clans".toSmallCaps(), TextDecoration.BOLD) }

    rowRenderer { clan, _ ->
        listOf(
            buildText {
                spacer(">")
                appendSpace()
                variableValue(clan.name)
                appendSpace()
                info("(${clan.tag})")

                clickEvent(clanClickEvent(clan.uuid))
            }
        )
    }
}

/**
 * Shows the information of the clan whose row was clicked.
 */
suspend fun handleClanListClick(clicked: Audience, clanUuid: UUID) {
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
