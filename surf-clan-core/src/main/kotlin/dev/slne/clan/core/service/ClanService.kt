package dev.slne.clan.core.service

import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.*

val clanService = requiredService<ClanService>()

interface ClanService {
    fun findClanByTag(tag: String): Clan?
    fun findClanByName(name: String): Clan?
    fun findClanByMember(uuid: UUID): Clan?
    fun findInvitesByMember(memberUuid: UUID): ObjectSet<ClanInvite>
    fun findClanByInvite(invite: ClanInvite): Clan?

    val clans: ObjectSet<Clan>

    suspend fun saveClan(clan: Clan): Clan
    suspend fun refreshCache()
    suspend fun deleteClan(clan: Clan)
    suspend fun createUnusedClanUuid(): UUID
}