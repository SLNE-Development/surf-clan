package dev.slne.clan.minestom

import com.google.auto.service.AutoService
import dev.slne.clan.minestom.command.ClanCommandRegistrar
import dev.slne.clan.minestom.listener.ClanTagColorJoinListener
import dev.slne.minestom.lobby.api.plugin.MinestomPlugin
import dev.slne.minestom.lobby.api.plugin.annotation.MinestomPluginMeta

@AutoService(MinestomPlugin::class)
@MinestomPluginMeta(
    "surf-clan-minestom",
    dependsOn = [
        "surf-api-minestom",
        "surf-rabbitmq-minestom",
        "surf-redis-minestom",
        "surf-chat-minestom"
    ]
)
class ClanMinestomPlugin : MinestomPlugin(ClanMinestomEntrypoint::class.java) {
    override fun configurePlugin() {
        bindCommandRegistrar<ClanCommandRegistrar>()
        bindEventRegistrar<ClanTagColorJoinListener>()
    }
}
