package dev.slne.clan.paper

import com.google.auto.service.AutoService
import dev.slne.clan.paper.commands.clanCommand
import dev.slne.surf.clan.core.ClanInstance

@AutoService(ClanInstance::class)
class PaperClanInstance : ClanInstance {
    override val dataPath get() = plugin.dataPath

    override suspend fun enable() {
        super.enable()
        clanCommand()
    }
}