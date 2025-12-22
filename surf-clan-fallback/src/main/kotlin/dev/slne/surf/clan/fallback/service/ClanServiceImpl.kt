package dev.slne.surf.clan.fallback.service

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.core.service.ClanService
import dev.slne.surf.clan.fallback.repository.clanRepository
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import net.kyori.adventure.util.Services
import java.util.*

@AutoService(ClanService::class)
class ClanServiceImpl : ClanService, Services.Fallback {
    private val clanCache = Caffeine.newBuilder().build<UUID, Clan>()

    override val clans get() = clanCache.asMap().values.toObjectSet()

    override fun findClanByTag(tag: String): Clan? =
        clans.find { it.tag.equals(tag, ignoreCase = true) }

    override fun findClanByName(name: String) =
        clans.find { it.name.equals(name, ignoreCase = true) }

    override fun findClanByMember(uuid: UUID) =
        clans.find { it.members.any { member -> member.uuid == uuid } }

    override fun findInvitesByMember(memberUuid: UUID) =
        clans.flatMap { it.invites }.filter { it.invited == memberUuid }.toObjectSet()

    override fun findClanByInvite(invite: ClanInvite) =
        clans.find { it.invites.any { clanInvite -> clanInvite == invite } }

    override suspend fun saveClan(clan: Clan): Clan {
        return clanRepository.save(clan).also { clanCache.put(clan.uuid, clan) }
    }

    override suspend fun refreshCache() {
        clanCache.invalidateAll()

        clanRepository.findClans().forEach { clan ->
            clanCache.put(clan.uuid, clan)
        }

        logger().atInfo().log("Loaded ${clanCache.asMap().size} clans into cache.")
    }

    override suspend fun deleteClan(clan: Clan) {
        clanRepository.delete(clan)
        clanCache.invalidate(clan.uuid)
    }

    override suspend fun createUnusedClanUuid(): UUID {
        var uuid: UUID

        do {
            uuid = UUID.randomUUID()
        } while (clans.any { it.uuid == uuid })

        return uuid
    }
}