package dev.slne.clan.paper

import com.google.auto.service.AutoService
import dev.slne.clan.core.ClanInstance
import dev.slne.clan.paper.commands.clanCommand

@AutoService(ClanInstance::class)
class PaperClanInstance : ClanInstance() {
    override val dataPath get() = plugin.dataPath

    override suspend fun enable() {
        super.enable()
        clanCommand()
    }
}