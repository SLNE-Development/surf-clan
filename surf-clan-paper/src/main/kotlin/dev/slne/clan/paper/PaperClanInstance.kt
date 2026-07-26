package dev.slne.clan.paper

import com.google.auto.service.AutoService
import dev.slne.clan.paper.commands.clanCommand
import dev.slne.clan.paper.commands.subcommands.ClanChatCommand
import dev.slne.clan.paper.listener.ClanTagColorJoinListener
import dev.slne.surf.api.paper.event.register
import dev.slne.surf.clan.core.ClanInstance
import dev.slne.surf.clan.core.client.ClientClanInstance

@AutoService(ClanInstance::class)
class PaperClanInstance : ClientClanInstance() {
    override val dataPath get() = plugin.dataPath

    override suspend fun enable() {
        super.enable()
        clanCommand()

        ClanTagColorJoinListener.register(plugin)

        if (plugin.checkSurfChat()) {
            ClanChatCommand("clanchat").register()
            ClanChatCommand("cc").register()
        }
    }
}