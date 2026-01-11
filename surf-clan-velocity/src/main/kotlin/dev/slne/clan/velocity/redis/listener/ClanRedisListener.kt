package dev.slne.clan.velocity.redis.listener

import com.github.shynixn.mccoroutine.velocity.launch
import dev.slne.clan.core.clan.CoreClanService
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.redis.event.BroadcastMessageEvent
import dev.slne.clan.velocity.redis.event.ClanBroadcastRedisEvent
import dev.slne.clan.velocity.redis.event.ClanInviteRedisEvent
import dev.slne.surf.redis.event.OnRedisEvent
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText
import net.kyori.adventure.text.event.ClickEvent
import kotlin.jvm.optionals.getOrNull

object ClanRedisListener {

    @OnRedisEvent
    fun onBroadcastMessage(event: BroadcastMessageEvent) {
        event.receiver
            .mapNotNull { plugin.proxy.getPlayer(it).getOrNull() }
            .forEach { it.sendMessage(event.message) }
    }

    @OnRedisEvent
    fun onClanBroadcastEvent(event: ClanBroadcastRedisEvent) {
        plugin.container.launch {
            val clan = CoreClanService.findClanByID(event.clanID) ?: return@launch
            for (member in clan.members) {
                val player = plugin.proxy.getPlayer(member.uuid).getOrNull() ?: continue
                player.sendMessage(event.message)
            }
        }
    }

    @OnRedisEvent
    fun onClanInviteEvent(event: ClanInviteRedisEvent) {
        val target = plugin.proxy.getPlayer(event.invitedUuid).getOrNull() ?: return

        target.sendText {
            appendPrefix()
            info("Du wurdest von ")
            variableValue(event.inviterName)
            info(" in den Clan ")
            variableValue(event.clanName)
            info(" eingeladen. ")

            append {
                success("[Annehmen]")
                hoverEvent(buildText {
                    info("Klicke hier, um die Einladung anzunehmen.")
                })
                clickEvent(ClickEvent.runCommand("/clan accept ${event.clanName}"))
            }

            appendSpace()

            append {
                error("[Ablehnen]")
                hoverEvent(buildText {
                    info("Klicke hier, um die Einladung abzulehnen.")
                })
                clickEvent(ClickEvent.runCommand("/clan deny ${event.clanName}"))
            }
        }
    }
}