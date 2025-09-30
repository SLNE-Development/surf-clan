package dev.slne.surf.clan.api.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction

open class ChangeMemberRoleArguments(
    target: ClanPlayer,
    targetMember: ClanMember,
    val oldRole: ClanMemberRole,
    val newRole: ClanMemberRole,
) : ClanAction.RequiredTargetActionArguments(target, targetMember)

interface ChangeMemberRoleAction : ClanAction<ChangeMemberRoleArguments>