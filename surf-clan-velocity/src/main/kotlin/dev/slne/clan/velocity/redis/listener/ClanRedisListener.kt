package dev.slne.clan.velocity.redis.listener

import dev.slne.clan.core.service.clanService
import dev.slne.clan.velocity.extensions.playerOrNull
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.redis.event.ClanBroadcastRedisEvent
import dev.slne.clan.velocity.redis.event.ClanInviteRedisEvent
import dev.slne.surf.redis.event.OnRedisEvent
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.NamedTextColor
import kotlin.jvm.optionals.getOrNull

object ClanRedisListener {
    @OnRedisEvent
    fun onClanBroadcastEvent(event: ClanBroadcastRedisEvent) {
        val clan = clanService.findClanByName(event.clanName) ?: return

        clan.members.forEach {
            it.playerOrNull?.sendText {
                append(event.message)
            }
        }
    }

    @OnRedisEvent
    fun onClanInviteEvent(event: ClanInviteRedisEvent) {
        val target = plugin.server.getPlayer(event.invitedUuid).getOrNull() ?: return

        target.sendText {
            appendPrefix()
            info("Du wurdest von ")
            variableValue(event.inviterName)
            info(" in den Clan ")
            variableValue(event.clanName)
            info(" eingeladen. ")

            val acceptComponent = buildText {
                append(Component.text("[Annehmen]", Colors.SUCCESS))
                hoverEvent(
                    HoverEvent.showText(
                        Component.text(
                            "Klicke hier, um die Einladung anzunehmen.",
                            NamedTextColor.GREEN
                        )
                    )
                )
                clickEvent(ClickEvent.runCommand("/clan accept ${event.clanName}"))
            }

            append(acceptComponent)
            appendSpace()

            val denyComponent = buildText {
                append(Component.text("[Ablehnen]", Colors.ERROR))
                hoverEvent(
                    HoverEvent.showText(
                        Component.text(
                            "Klicke hier, um die Einladung abzulehnen.",
                            NamedTextColor.RED
                        )
                    )
                )
                clickEvent(ClickEvent.runCommand("/clan deny ${event.clanName}"))
            }

            append(denyComponent)
        }

    }
}