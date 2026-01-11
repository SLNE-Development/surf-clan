package dev.slne.surf.clan.placeholder.expansion

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.expireAfterWrite
import com.sksamuel.aedile.core.refreshAfterWrite
import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.listener.ClanUpdatedListener
import net.kyori.adventure.text.Component
import java.util.*
import kotlin.time.Duration.Companion.minutes
import kotlin.time.Duration.Companion.seconds

object ClanDataCache : ClanUpdatedListener {
    private val cache = Caffeine.newBuilder()
        .refreshAfterWrite(30.seconds)
        .expireAfterWrite(10.minutes)
        .maximumSize(30_000)
        .asLoadingCache<UUID, CachedClanData> { uuid ->
            loadClanData(uuid)
        }

    private suspend fun loadClanData(uuid: UUID): CachedClanData {
        val clan = Clan.byPlayer(uuid) ?: return CachedClanData.Empty
        return CachedClanData.Loaded(clan.name, clan.tag, clan.renderClanTag(minSize = 10))
    }

    fun getData(uuid: UUID): CachedClanData {
        return cache.underlying().get(uuid).getNow(CachedClanData.Empty)
    }

    override fun onClanUpdated(clan: Clan) {
        clan.members.forEach { member ->
            val key = member.uuid
            if (cache.underlying().getIfPresent(key) != null) {
                cache.underlying().synchronous().refresh(key)
            }
        }
    }

    sealed interface CachedClanData {
        data object Empty : CachedClanData
        data class Loaded(
            val name: String,
            val tag: String,
            val renderedClanTag: Component
        ) : CachedClanData
    }
}