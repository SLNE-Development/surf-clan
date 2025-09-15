package dev.slne.surf.clan.core.common.clan

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.ClanManager
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.cloud.api.common.sync.SyncSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.util.*

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

    suspend fun internalInviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult {
        val result = clan.canInvite(target, player)

        if (result.isError) {
            return result
        }

        return inviteMember(clan, player, target)
    }

    abstract suspend fun inviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult

    suspend fun internalUninviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult {
        val result = clan.canUninvite(target, player)

        if (result.isError) {
            return result as ClanMemberUninviteResult
        }

        return uninviteMember(clan, player, target)
    }

    abstract suspend fun uninviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult

    suspend fun internalAddMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer,
        role: ClanMemberRole,
    ): ComponentResult {
        val result = clan.canAddMember(player, target, role)

        if (result.isError) {
            return result
        }

        return addMember(clan, player, target, role)
    }

    abstract suspend fun addMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer,
        role: ClanMemberRole,
    ): ComponentResult

    suspend fun internalRemoveMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult {
        val result = clan.canRemoveMember(player, target)

        if (result.isError) {
            return result
        }

        return removeMember(clan, player, target)
    }

    abstract suspend fun removeMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult

    suspend fun internalSetName(
        clan: Clan,
        player: ClanPlayer,
        name: String,
    ): ComponentResult {
        val result = clan.canSetName(player, name)

        if (result.isError) {
            return result
        }

        return setName(clan, player, name)
    }

    abstract suspend fun setName(
        clan: Clan,
        player: ClanPlayer,
        name: String,
    ): ComponentResult

    suspend fun internalSetTag(
        clan: Clan,
        player: ClanPlayer,
        tag: ClanTag,
    ): ComponentResult {
        val result = clan.canSetTag(player, tag)

        if (result.isError) {
            return result
        }

        return setTag(clan, player, tag)
    }

    abstract suspend fun setTag(
        clan: Clan,
        player: ClanPlayer,
        tag: ClanTag,
    ): ComponentResult

    suspend fun internalSetDiscordInvite(
        clan: Clan,
        player: ClanPlayer,
        invite: String?,
    ): ComponentResult {
        val result = clan.canSetDiscordInvite(player, invite)

        if (result.isError) {
            return result
        }

        return setDiscordInvite(clan, player, invite)
    }

    abstract suspend fun setDiscordInvite(
        clan: Clan,
        player: ClanPlayer,
        invite: String?,
    ): ComponentResult
}