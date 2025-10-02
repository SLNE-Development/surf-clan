package dev.slne.surf.clan.core.common.clan

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.ClanManager
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.cloud.api.common.sync.SyncSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.springframework.stereotype.Component
import java.util.*

@Component("clanManager")
abstract class ClanManagerCommon : ClanManager {
    private val _clans = SyncSet<Clan>("clan:clans")
    val clans get() = _clans.snapshot()

    override fun getClanByUuid(uuid: UUID) = _clans.find { it.uuid == uuid }
    override fun getClanByName(name: String) = _clans.find { it.name.equals(name, true) }
    override fun getClanByTag(tag: String) = _clans.find { it.fullTag.tag.equals(tag, true) }
    override fun getClanByPlayer(player: ClanPlayer) = _clans.find { it.isMember(player) }

    protected fun clearCache() = _clans.clear()
    protected fun addAllToCache(clans: Collection<Clan>) = this._clans.addAll(clans)

    abstract suspend fun findAllClans(): ObjectSet<ClanCommon>

    abstract suspend fun inviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult

    abstract suspend fun uninviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult

    abstract suspend fun addMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer,
        role: ClanMemberRole,
    ): ComponentResult

    abstract suspend fun removeMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult

    abstract suspend fun setName(
        clan: Clan,
        player: ClanPlayer,
        name: String,
    ): ComponentResult

    abstract suspend fun setTag(
        clan: Clan,
        player: ClanPlayer,
        tag: ClanTag,
    ): ComponentResult

    abstract suspend fun setDiscordInvite(
        clan: Clan,
        player: ClanPlayer,
        invite: String?,
    ): ComponentResult
}