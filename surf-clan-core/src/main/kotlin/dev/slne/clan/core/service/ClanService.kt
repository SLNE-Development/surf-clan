package dev.slne.clan.core.service

import dev.slne.clan.api.Clan
import dev.slne.clan.core.entities.ClanEntity
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.util.*

object ClanService {

    private val clanCache = mutableObjectSetOf<Clan>()
    val clans = clanCache.freeze()

    suspend fun fetchClans() = newSuspendedTransaction(Dispatchers.IO) {
        clanCache.clear()
        clanCache.addAll(ClanEntity.all().map { it.toClan() })
    }

    suspend fun saveClan(clan: Clan): Nothing = newSuspendedTransaction(Dispatchers.IO) {
        TODO("Implement")
    }

    suspend fun deleteClan(clan: Clan): Nothing = newSuspendedTransaction(Dispatchers.IO) {
        TODO("Implement")
    }

    fun createUnusedClanUuid(): UUID {
        var uuid = UUID.randomUUID()

        while (clanCache.any { it.uuid == uuid }) {
            uuid = UUID.randomUUID()
        }

        return uuid
    }

    fun findClanByTag(tag: String) = clanCache.find { it.tag == tag }
    fun findClanByName(name: String) = clanCache.find { it.name == name }

    fun findClanByMemberUniqueId(uuid: UUID): Clan? = clanCache.find { clan ->
        clan.members.any { it.player.uuid == uuid }
    }

    fun findClanInvitesByMemberUniqueId(uuid: UUID) = clanCache.flatMap { clan ->
        clan.invites.filter { it.invited.uuid == uuid }
    }

}