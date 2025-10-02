@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.core.common.clan

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.*
import dev.slne.surf.clan.api.common.clan.actions.member.*
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.api.common.util.InternalClanApi
import dev.slne.surf.clan.core.common.clan.actions.ChangeClanTagActionCommon
import dev.slne.surf.clan.core.common.clan.actions.ChangeDiscordInviteActionCommon
import dev.slne.surf.clan.core.common.clan.actions.ClanActionManager
import dev.slne.surf.clan.core.common.clan.invite.ClanInviteCommon
import dev.slne.surf.clan.core.common.clan.member.ClanMemberCommon
import dev.slne.surf.cloud.api.common.util.toObjectSet
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import org.springframework.beans.factory.getBean
import java.time.ZonedDateTime
import java.util.*

private val clanActionManager
    get() = InternalContextHolder.context.getBean<ClanActionManager>()

@Serializable
class ClanCommon(
    override val uuid: @Contextual UUID,
    override val name: String,
    override val fullTag: ClanTag,
    override val createdByUuid: @Contextual UUID,
    override var discordInvite: String? = null,
    private val _members: Set<ClanMemberCommon>,
    private val _invites: Set<ClanInviteCommon>,
    override val createdAt: @Contextual ZonedDateTime,
    override val updatedAt: @Contextual ZonedDateTime
) : Clan {
    override val members get() = _members.toObjectSet()
    override val invites get() = _invites.toObjectSet()

    override suspend fun invite(
        player: ClanPlayer,
        target: ClanPlayer
    ) = clanActionManager.execute<InviteMemberAction, InviteMemberArguments>(
        this,
        player,
        InviteMemberArguments(
            target = target
        )
    )

    override suspend fun uninvite(
        player: ClanPlayer,
        target: ClanPlayer
    ) = clanActionManager.execute<UninviteMemberAction, UninviteMemberArguments>(
        this,
        player,
        UninviteMemberArguments(
            target = target
        )
    )

    override fun isInvited(player: ClanPlayer) = _invites.any { it.invitedUuid == player.uuid }

    override fun getMember(player: ClanPlayer) =
        _members.firstOrNull { it.uuid == player.uuid }

    override fun isMember(player: ClanPlayer) =
        getMember(player) != null

    override suspend fun addMember(
        player: ClanPlayer,
        role: ClanMemberRole,
        target: ClanPlayer,
    ) = clanActionManager.execute<AddMemberAction, AddMemberArguments>(
        this,
        player,
        AddMemberArguments(
            target = target
        )
    )

    override suspend fun removeMember(
        player: ClanPlayer,
        member: ClanMember,
    ) = clanActionManager.execute<RemoveMemberAction, RemoveMemberArguments>(
        this,
        player,
        RemoveMemberArguments(
            target = member.clanPlayer(),
            targetMember = member
        )
    )

    override suspend fun setName(
        player: ClanPlayer,
        name: String,
    ) = clanActionManager.execute<RenameClanAction, RenameClanArguments>(
        this,
        player,
        RenameClanArguments(
            oldName = this.name,
            newName = name
        )
    )

    override suspend fun setTag(
        player: ClanPlayer,
        tag: ClanTag,
    ) = clanActionManager.execute<ChangeClanTagActionCommon, ChangeClanTagArguments>(
        this,
        player,
        ChangeClanTagArguments(
            oldTag = this.fullTag,
            newTag = tag
        )
    )

    override suspend fun setDiscordInvite(
        player: ClanPlayer,
        invite: String?,
    ) = clanActionManager.execute<ChangeDiscordInviteActionCommon, ChangeDiscordInviteArguments>(
        this,
        player,
        ChangeDiscordInviteArguments(
            oldInvite = this.discordInvite,
            newInvite = invite
        )
    )

    override fun hasPermission(clanPlayer: ClanPlayer, permission: ClanPermission): Boolean {
        val member = getMember(clanPlayer) ?: return false

        return member.hasPermission(permission)
    }

    override suspend fun <Action : ClanAction<Arguments>, Arguments> authorize(
        actionClass: Class<Action>,
        player: ClanPlayer,
        arguments: Arguments
    ): ComponentResult = clanActionManager.authorize(
        actionClass,
        this,
        player,
        arguments
    )

    override suspend fun disbandClan(
        player: ClanPlayer,
        clan: Clan
    ) = clanActionManager.execute<DisbandClanAction, ClanAction.EmptyArguments>(
        this,
        player,
        ClanAction.EmptyArguments()
    )

    override fun getMembersWithRole(role: ClanMemberRole) =
        _members.filter { it.role == role }.toObjectSet()

    override fun asComponent() = buildText {
        variableValue(name)
        primary(" TODO")
    }
}