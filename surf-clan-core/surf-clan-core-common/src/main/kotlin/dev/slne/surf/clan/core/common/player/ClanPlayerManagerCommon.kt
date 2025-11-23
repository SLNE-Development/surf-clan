package dev.slne.surf.clan.core.common.player

import com.github.benmanes.caffeine.cache.Caffeine
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.expireAfterAccess
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.player.ClanPlayerManager
import dev.slne.surf.clan.api.common.util.ComponentResult
import java.util.*
import kotlin.time.Duration.Companion.minutes

abstract class ClanPlayerManagerCommon : ClanPlayerManager {
    private val cache = Caffeine.newBuilder()
        .expireAfterAccess(30.minutes)
        .asLoadingCache<UUID, ClanPlayer> {
            findOrCreatePlayer(it)
        }

    override suspend fun getPlayer(uuid: UUID) = cache.get(uuid)

    abstract suspend fun findOrCreatePlayer(uuid: UUID): ClanPlayer

    abstract suspend fun setMemberRole(
        clan: Clan,
        player: ClanPlayer,
        targetMember: ClanMember,
        role: ClanMemberRole,
    ): ComponentResult

    abstract suspend fun setAcceptsClanInvites(
        player: ClanPlayer,
        value: Boolean
    ): Boolean
}