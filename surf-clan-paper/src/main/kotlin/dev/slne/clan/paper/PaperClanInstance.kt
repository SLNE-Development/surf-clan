package dev.slne.clan.paper

import com.google.auto.service.AutoService
import dev.slne.clan.core.ClanInstance

@AutoService(ClanInstance::class)
class PaperClanInstance : ClanInstance() {
    override val dataPath get() = plugin.dataPath
}