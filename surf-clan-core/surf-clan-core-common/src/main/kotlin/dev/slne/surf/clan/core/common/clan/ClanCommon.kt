@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.core.common.clan

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.invite.ClanInvite
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.InternalClanApi
import dev.slne.surf.cloud.api.common.util.freeze
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.springframework.beans.factory.getBean
import java.time.ZonedDateTime
import java.util.*

class ClanCommon(
    override val uuid: UUID,
    override val name: String,
    override val fullTag: ClanTag,
    override val createdByUuid: UUID,
    override var discordInvite: String? = null,
    members: ObjectSet<ClanMember>,
    invites: ObjectSet<ClanInvite>,
    override val createdAt: ZonedDateTime,
    override val updatedAt: ZonedDateTime
) : Clan {

    private val clanManager by lazy {
        InternalContextHolder.context.getBean<ClanManagerCommon>()
    }

    private val _members = members
    override val members get() = _members.freeze()

    private val _invites = invites
    override val invites get() = _invites.freeze()

    override suspend fun invite(
        player: ClanPlayer,
        invitedBy: ClanPlayer
    ) = clanManager.inviteMember(this, player, invitedBy)

    override suspend fun uninvite(player: ClanPlayer, uninvitedBy: ClanPlayer) =
        clanManager.uninviteMember(this, player, uninvitedBy)

    override fun isInvited(player: ClanPlayer) = _invites.any { it.invitedUuid == player.uuid }

    override fun getMember(player: ClanPlayer) =
        _members.firstOrNull { it.uuid == player.uuid }

    override fun isMember(player: ClanPlayer) =
        getMember(player) != null

    override suspend fun addMember(
        player: ClanPlayer,
        role: ClanMemberRole,
        addedBy: ClanPlayer
    ) = clanManager.addMember(this, player, role, addedBy)

    override suspend fun removeMember(member: ClanMember, removedBy: ClanPlayer) =
        clanManager.removeMember(this, member.clanPlayer(), removedBy)

    override suspend fun setName(name: String, setBy: ClanPlayer) =
        clanManager.setName(this, name, setBy)

    override suspend fun setTag(tag: ClanTag, setBy: ClanPlayer) =
        clanManager.setTag(this, tag, setBy)

    override suspend fun setDiscordInvite(discordInvite: String?, setBy: ClanPlayer) =
        clanManager.setDiscordInvite(this, discordInvite, setBy)

    override fun hasPermission(clanPlayer: ClanPlayer, permission: ClanPermission): Boolean {
        val member = getMember(clanPlayer) ?: return false

        return member.hasPermission(permission)
    }

    override fun asComponent() = buildText {
        variableValue(name)
        primary(" TODO")
    }
}