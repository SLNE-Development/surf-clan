package dev.slne.clan.velocity.commands.subcommands.admin.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.slne.clan.velocity.VelocityMain.Companion.redisApi
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.redis.event.ClanRefreshRedisEvent
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class ClanRefreshCommand : CommandAPICommand("refresh") {
    init {
        withPermission("surf.clan.admin.refresh")

        anyExecutor { player, _ ->
            plugin.container.launch {

                val count = redisApi.publishEvent(ClanRefreshRedisEvent).await()

                player.sendText {
                    appendPrefix()
                    success("Der Clan-Cache wurde auf ")
                    variableValue(count)
                    success(" Servern aktualisiert.")
                }
            }
        }
    }
}