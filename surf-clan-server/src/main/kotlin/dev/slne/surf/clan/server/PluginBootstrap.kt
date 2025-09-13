package dev.slne.surf.clan.server

import dev.slne.surf.clan.ClanApplication
import dev.slne.surf.clan.core.common.InternalContextHolderImpl
import dev.slne.surf.cloud.api.common.CloudInstance
import dev.slne.surf.cloud.api.common.startSpringApplication
import dev.slne.surf.cloud.api.server.plugin.bootstrap.BootstrapContext
import dev.slne.surf.cloud.api.server.plugin.bootstrap.StandalonePluginBootstrap

class PluginBootstrap : StandalonePluginBootstrap {
    override suspend fun bootstrap(context: BootstrapContext) {
        InternalContextHolderImpl.INSTANCE.context =
            CloudInstance.startSpringApplication(ClanApplication::class)
    }
}