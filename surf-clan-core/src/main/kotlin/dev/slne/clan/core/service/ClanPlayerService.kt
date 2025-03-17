package dev.slne.clan.core.service

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.expireAfterAccess
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.clan.core.entities.ClanPlayerEntity
import dev.slne.clan.core.entities.ClanPlayerTable
import dev.slne.clan.core.player.CoreClanPlayer
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*
import kotlin.coroutines.CoroutineContext
import kotlin.time.Duration.Companion.minutes

object ClanPlayerService {

    private val clanPlayerCache = Caffeine
        .newBuilder()
        .expireAfterAccess(10.minutes)
        .build<UUID, ClanPlayer> { key ->
            CoreClanPlayer(uuid = key)
        }

    suspend fun fetchClanPlayers() = newSuspendedTransaction {
        clanPlayerCache.invalidateAll()
        clanPlayerCache.putAll(ClanPlayerEntity.all().associate { it.uuid to it.toClanPlayer() })
    }

    suspend fun findOrCreateClanPlayerEntity(
        clanPlayer: ClanPlayer,
        context: CoroutineContext? = null
    ) = newSuspendedTransaction(context) {
        ClanPlayerEntity.find { ClanPlayerTable.uuid eq clanPlayer.uuid }.firstOrNull()
            ?: ClanPlayerEntity.new {
                this.uuid = clanPlayer.uuid
                this.username = clanPlayer.username
                this.acceptsClanInvites = clanPlayer.acceptsClanInvites
            }
    }

    fun findClanPlayer(username: String) =
        clanPlayerCache.asMap().values.find { it.username == username }

    fun findClanPlayer(uuid: UUID) = clanPlayerCache[uuid]
}