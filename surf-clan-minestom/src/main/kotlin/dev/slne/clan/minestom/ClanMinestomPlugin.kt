package dev.slne.clan.minestom

import dev.slne.clan.minestom.command.clanChatCommand
import dev.slne.clan.minestom.command.clanCommand
import dev.slne.clan.minestom.listener.ClanTagColorJoinListener
import dev.slne.surf.api.minestom.plugin.SurfMinestomPlugin
import dev.slne.surf.clan.core.ClanInstance

object ClanMinestomPlugin : SurfMinestomPlugin() {
    override suspend fun onLoad() {
        ClanInstance.load()
        ClanInstance.enable()
    }

    override suspend fun onEnable() {
        clanCommand()

        clanChatCommand("clanchat").register()
        clanChatCommand("cc").register()

        ClanTagColorJoinListener.register(eventNode)
    }

    override suspend fun onDisable() {
        ClanInstance.disable()
    }
}
