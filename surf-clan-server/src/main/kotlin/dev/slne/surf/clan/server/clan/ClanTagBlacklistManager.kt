package dev.slne.surf.clan.server.clan

import dev.slne.surf.clan.server.db.entities.ClanTagBlacklistDto
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import org.springframework.stereotype.Component

@Component
class ClanTagBlacklistManager {

    private val _blacklist = mutableObjectSetOf<ClanTagBlacklistDto>()
    val blacklist get() = _blacklist.freeze()

    fun fetch() {
        _blacklist.clear()
        _blacklist.addAll(ClanTagBlacklistRepository().fetchAllBlacklistedTags())
    }

    fun getTagBlacklist(tag: String) = _blacklist.find { it.tag == tag }

}