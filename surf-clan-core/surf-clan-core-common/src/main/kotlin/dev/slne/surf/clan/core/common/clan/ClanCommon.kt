@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.core.common.clan

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.ChangeClanTagAction
import dev.slne.surf.clan.api.common.clan.actions.ChangeDiscordInviteAction
import dev.slne.surf.clan.api.common.clan.actions.DisbandClanAction
import dev.slne.surf.clan.api.common.clan.actions.RenameClanAction
import dev.slne.surf.clan.api.common.clan.actions.member.AddMemberAction
import dev.slne.surf.clan.api.common.clan.actions.member.ChangeMemberRoleAction
import dev.slne.surf.clan.api.common.clan.actions.member.InviteMemberAction
import dev.slne.surf.clan.api.common.clan.actions.member.RemoveMemberAction
import dev.slne.surf.clan.api.common.clan.executeAuthorizing
import dev.slne.surf.clan.api.common.clan.invite.ClanInvite
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.api.common.util.ClanActionExtraArguments
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.api.common.util.InternalClanApi
import dev.slne.surf.clan.core.common.clan.actions.RenameClanActionCommon
import dev.slne.surf.clan.core.common.clan.actions.member.ChangeMemberRoleActionCommon
import dev.slne.surf.cloud.api.common.util.freeze
import dev.slne.surf.cloud.api.common.util.toObjectSet
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.springframework.beans.factory.getBean
import java.time.ZonedDateTime
import java.util.*
import kotlin.reflect.KClass

private val authorizationMap = mutableMapOf(
    RenameClanAction::class to RenameClanActionCommon,

    // Member
    ChangeMemberRoleAction::class to ChangeMemberRoleActionCommon
)

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
        target: ClanPlayer
    ) = executeAuthorizing<InviteMemberAction>(target, ("target" to player)) {
        clanManager.inviteMember(this, player, target)
    }

    override suspend fun uninvite(
        player: ClanPlayer,
        target: ClanPlayer
    ) = executeAuthorizing<InviteMemberAction>(target, ("target" to player)) {
        clanManager.uninviteMember(this, player, target)
    }

    override fun isInvited(player: ClanPlayer) = _invites.any { it.invitedUuid == player.uuid }

    override fun getMember(player: ClanPlayer) =
        _members.firstOrNull { it.uuid == player.uuid }

    override fun isMember(player: ClanPlayer) =
        getMember(player) != null

    override suspend fun addMember(
        player: ClanPlayer,
        role: ClanMemberRole,
        addedBy: ClanPlayer
    ) = executeAuthorizing<AddMemberAction>(
        addedBy,
        ("target" to player),
        ("role" to role)
    ) {
        clanManager.addMember(this, player, role, addedBy)
    }

    override suspend fun removeMember(
        member: ClanMember,
        removedBy: ClanPlayer
    ) = executeAuthorizing<RemoveMemberAction>(removedBy, ("target" to member)) {
        clanManager.removeMember(this, member.clanPlayer(), removedBy)
    }

    override suspend fun setName(
        name: String,
        setBy: ClanPlayer
    ) = executeAuthorizing<RenameClanAction>(setBy, ("name" to name)) {
        clanManager.setName(this, name, setBy)
    }

    override suspend fun setTag(
        tag: ClanTag,
        setBy: ClanPlayer
    ) = executeAuthorizing<ChangeClanTagAction>(setBy, ("tag" to tag)) {
        clanManager.setTag(this, tag, setBy)
    }

    override suspend fun setDiscordInvite(
        discordInvite: String?,
        setBy: ClanPlayer
    ) = executeAuthorizing<ChangeDiscordInviteAction>(setBy, ("discordInvite" to discordInvite)) {
        clanManager.setDiscordInvite(this, discordInvite, setBy)
    }


    override

    fun hasPermission(clanPlayer: ClanPlayer, permission: ClanPermission): Boolean {
        val member = getMember(clanPlayer) ?: return false

        return member.hasPermission(permission)
    }

    override suspend fun authorize(
        actionClass: KClass<out ClanAction>,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ) = authorizationMap[actionClass]?.authorize(this, player, arguments)
        ?: ComponentResult.NoPolicyFound(this, player.uuid, actionClass)

    override suspend fun executeAuthorizing(
        actionClass: KClass<out ClanAction>,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments,
        action: suspend () -> ComponentResult
    ): ComponentResult {
        val authorization = authorize(actionClass, player, arguments)

        if (authorization.isError) {
            return authorization
        }

        return action()
    }

    override suspend fun disbandClan(
        clan: Clan,
        disbandedBy: ClanPlayer
    ) = executeAuthorizing<DisbandClanAction>(disbandedBy) {
        TODO("Implement disbandClan")
    }

    override fun getMembersWithRole(role: ClanMemberRole) =
        _members.filter { it.role == role }.toObjectSet()

    override fun asComponent() = buildText {
        variableValue(name)
        primary(" TODO")
    }
}