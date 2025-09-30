@file:OptIn(InternalClanApi::class)

package dev.slne.surf.clan.core.common.clan

import dev.slne.surf.clan.api.common.InternalContextHolder
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.*
import dev.slne.surf.clan.api.common.clan.actions.member.*
import dev.slne.surf.clan.api.common.clan.invite.ClanInvite
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.api.common.util.InternalClanApi
import dev.slne.surf.clan.core.common.clan.actions.ChangeClanTagActionCommon
import dev.slne.surf.clan.core.common.clan.actions.ChangeDiscordInviteActionCommon
import dev.slne.surf.clan.core.common.clan.actions.ClanActionProcessor
import dev.slne.surf.clan.core.common.clan.actions.execute
import dev.slne.surf.cloud.api.common.util.freeze
import dev.slne.surf.cloud.api.common.util.toObjectSet
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import it.unimi.dsi.fastutil.objects.ObjectSet
import org.springframework.beans.factory.getBean
import java.time.ZonedDateTime
import java.util.*
import kotlin.reflect.KClass

private val clanActionProcessor
    get() = InternalContextHolder.context.getBean<ClanActionProcessor>()

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
    private val _members = members
    override val members get() = _members.freeze()

    private val _invites = invites
    override val invites get() = _invites.freeze()

    override suspend fun invite(
        player: ClanPlayer,
        target: ClanPlayer
    ) = clanActionProcessor.execute<InviteMemberAction, InviteMemberArguments>(
        this,
        player,
        InviteMemberArguments(
            target = target
        )
    )

    override suspend fun uninvite(
        player: ClanPlayer,
        target: ClanPlayer
    ) = clanActionProcessor.execute<UninviteMemberAction, UninviteMemberArguments>(
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
    ) = clanActionProcessor.execute<AddMemberAction, AddMemberArguments>(
        this,
        player,
        AddMemberArguments(
            target = target
        )
    )

    override suspend fun removeMember(
        player: ClanPlayer,
        member: ClanMember,
    ) = clanActionProcessor.execute<RemoveMemberAction, RemoveMemberArguments>(
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
    ) = clanActionProcessor.execute<RenameClanAction, RenameClanArguments>(
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
    ) = clanActionProcessor.execute<ChangeClanTagActionCommon, ChangeClanTagArguments>(
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
    ) = clanActionProcessor.execute<ChangeDiscordInviteActionCommon, ChangeDiscordInviteArguments>(
        this,
        player,
        ChangeDiscordInviteArguments(
            oldInvite = this.discordInvite,
            newInvite = invite
        )
    )

    override suspend fun <Action : ClanAction<Arguments>, Arguments : Any> authorize(
        actionClass: KClass<out Action>,
        player: ClanPlayer,
        arguments: Arguments
    ) = clanActionProcessor.authorize(
        actionClass,
        this,
        player,
        arguments
    )

    override fun hasPermission(clanPlayer: ClanPlayer, permission: ClanPermission): Boolean {
        val member = getMember(clanPlayer) ?: return false

        return member.hasPermission(permission)
    }

    override suspend fun disbandClan(
        player: ClanPlayer,
        clan: Clan
    ) = clanActionProcessor.execute<DisbandClanAction, ClanAction.EmptyArguments>(
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