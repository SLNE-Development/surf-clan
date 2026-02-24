package dev.slne.clan.paper

import com.google.auto.service.AutoService
import dev.slne.clan.core.ClanInstance
import dev.slne.clan.paper.commands.clanCommand
import dev.slne.clan.paper.listener.PlayerActivityListener

@AutoService(ClanInstance::class)
class PaperClanInstance : ClanInstance() {
    override val dataPath get() = plugin.dataPath

    override suspend fun enable() {
        super.enable()
        clanCommand()
        plugin.server.pluginManager.registerEvents(PlayerActivityListener, plugin)
    }
}