package dev.slne.surf.clan.fallback.service

import com.google.auto.service.AutoService
import dev.slne.clan.api.Clan
import dev.slne.clan.api.ClanModificationListener
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.core.service.ClanService
import dev.slne.surf.clan.fallback.repository.clanRepository
import dev.slne.surf.redis.RedisApi
import dev.slne.surf.redis.sync.map.SyncMap
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import net.kyori.adventure.util.Services
import java.util.*

@AutoService(ClanService::class)
class ClanServiceImpl : ClanService, Services.Fallback {
    private lateinit var globalClans: SyncMap<String, Clan>
    override val clans get() = globalClans.snapshot().values.toObjectSet()

    private val clanListeners = mutableObjectSetOf<ClanModificationListener>()

    override suspend fun refreshClans() {
        globalClans.clear()
        clanRepository.findClans().forEach { globalClans.put(it.uuid.toString(), it) }
    }

    override fun load(redisApi: RedisApi) {
        globalClans = redisApi.createSyncMap("surf-clan:clans")
    }

    override fun addClanModificationListener(
        listener: ClanModificationListener
    ) {
        clanListeners.add(listener)
    }

    override fun findClanByTag(tag: String): Clan? =
        clans.find { it.tag.equals(tag, ignoreCase = true) }

    override fun findClanByName(name: String) =
        clans.find { it.name.equals(name, ignoreCase = true) }

    override fun findClanByMember(uuid: UUID) =
        clans.find { it.members.any { member -> member.uuid == uuid } }

    override fun findClanByUuid(clanUuid: UUID): Clan? =
        clans.find { it.uuid == clanUuid }

    override fun findInvitesByMember(memberUuid: UUID) =
        clans.flatMap { it.invites }.filter { it.invited == memberUuid }.toObjectSet()

    override fun findClanByInvite(invite: ClanInvite) =
        clans.find { it.invites.any { clanInvite -> clanInvite == invite } }

    override suspend fun saveClan(clan: Clan): Clan {
        clanListeners.filter { it.clanUuid == clan.uuid }.forEach {
            it.action.invoke(clan)
        }

        return clanRepository.save(clan).also { globalClans.put(clan.uuid.toString(), clan) }
    }

    override suspend fun deleteClan(clan: Clan) {
        clanRepository.delete(clan)
        clans.find { it.uuid == clan.uuid }?.let {
            globalClans.remove(it.uuid.toString())
        }
    }

    override suspend fun createUnusedClanUuid(): UUID {
        var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (clans.any { it.uuid == uuid })

        return uuid
    }
}