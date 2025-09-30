package dev.slne.surf.clan.server.clan

import dev.slne.surf.clan.server.db.entities.ClanTagBlacklistEntity
import dev.slne.surf.cloud.api.server.plugin.CoroutineTransactional
import org.springframework.stereotype.Repository

@Repository
@CoroutineTransactional
class ClanTagBlacklistRepository {
    fun fetchAllBlacklistedTags() = ClanTagBlacklistEntity.all().map { it.toDto() }
}