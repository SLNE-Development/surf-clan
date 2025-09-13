package dev.slne.surf.clan.paper

import dev.slne.surf.clan.ClanApplication
import dev.slne.surf.clan.core.common.InternalContextHolderImpl
import dev.slne.surf.cloud.api.common.CloudInstance
import dev.slne.surf.cloud.api.common.startSpringApplication
import io.papermc.paper.plugin.bootstrap.BootstrapContext
import io.papermc.paper.plugin.bootstrap.PluginBootstrap

@Suppress("UnstableApiUsage")
class PaperBootstrap : PluginBootstrap {
    override fun bootstrap(context: BootstrapContext) {
        InternalContextHolderImpl.INSTANCE.context =
            CloudInstance.startSpringApplication(ClanApplication::class)
    }
}