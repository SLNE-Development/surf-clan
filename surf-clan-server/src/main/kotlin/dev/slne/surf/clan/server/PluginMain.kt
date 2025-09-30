package dev.slne.surf.clan.server

import dev.slne.surf.clan.server.clan.ClanManagerServer
import dev.slne.surf.clan.server.clan.ClanTagBlacklistManager
import dev.slne.surf.cloud.api.server.plugin.StandalonePlugin
import dev.slne.surf.cloud.api.server.plugin.utils.bean

class PluginMain : StandalonePlugin() {
    override suspend fun load() {

    }

    override suspend fun enable() {
        bean<ClanManagerServer>().cacheAllClans()
        bean<ClanTagBlacklistManager>().fetch()
    }

    override suspend fun disable() {

    }
}

val plugin get() = StandalonePlugin.getPlugin(PluginMain::class)