package dev.slne.surf.clan.core.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.ChangeMemberRoleAction
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.result.role.ClanMemberSetRoleResult
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanActionExtraArguments
import dev.slne.surf.clan.api.common.util.ComponentResult

object ChangeMemberRoleActionCommon : ChangeMemberRoleAction {
    override val permission = ClanPermission.MEMBER_PROMOTE

    override suspend fun authorizeExtra(
        clan: Clan,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ): ComponentResult? {
        val selfMember: ClanMember by arguments
        val target: ClanPlayer by arguments
        val oldRole: ClanMemberRole by arguments
        val newRole: ClanMemberRole by arguments

        if (player == target) {
            return ClanMemberSetRoleResult.CannotChangeOwnRole(clan)
        }

        val otherMember = clan.getMember(target)
            ?: return ComponentResult.OtherNotClanMember(clan, player.uuid, target.uuid)

        if (otherMember >= selfMember) {
            return ClanMemberSetRoleResult.OtherSameOrHigherRole(
                clan,
                selfMember,
                otherMember,
                true
            )
        }

        return ClanMemberSetRoleResult.Success(clan, otherMember, oldRole, newRole)
    }
}