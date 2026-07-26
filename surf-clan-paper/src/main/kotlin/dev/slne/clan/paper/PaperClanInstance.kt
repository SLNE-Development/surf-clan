package dev.slne.clan.paper

import com.google.auto.service.AutoService
import dev.slne.clan.paper.commands.clanCommand
import dev.slne.clan.paper.commands.subcommands.ClanChatCommand
import dev.slne.clan.paper.listener.ClanTagColorJoinListener
import dev.slne.surf.api.core.util.logger
import dev.slne.surf.api.paper.event.register
import dev.slne.surf.clan.core.ClanInstance
import dev.slne.surf.clan.core.client.ClientClanInstance

@AutoService(ClanInstance::class)
class PaperClanInstance : ClientClanInstance() {
    private val log = logger()

    override val dataPath get() = plugin.dataPath

    override suspend fun enable() {
        super.enable()
        clanCommand()

        if (plugin.checkLuckPerms()) {
            ClanTagColorJoinListener.register(plugin)
        } else {
            log.atWarning()
                .log("LuckPerms is not enabled, clan tag colors will not be enforced")
        }

        if (plugin.checkSurfChat()) {
            ClanChatCommand("clanchat").register()
            ClanChatCommand("cc").register()
        }
    }
}