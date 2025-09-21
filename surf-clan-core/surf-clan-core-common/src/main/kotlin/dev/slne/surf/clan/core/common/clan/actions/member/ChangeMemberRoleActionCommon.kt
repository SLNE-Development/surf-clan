package dev.slne.surf.clan.core.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.ChangeMemberRoleAction
import dev.slne.surf.clan.api.common.clan.actions.member.ChangeMemberRoleArguments
import dev.slne.surf.clan.api.common.clan.member.result.role.ClanMemberSetRoleResult
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.stereotype.Component

@Component
class ChangeMemberRoleActionCommon : ClanActionCommon<ChangeMemberRoleArguments>(),
    ChangeMemberRoleAction {
    override val permission = ClanPermission.MEMBER_PROMOTE

    override suspend fun authorize(
        clan: Clan,
        player: ClanPlayer,
        arguments: ChangeMemberRoleArguments
    ): ComponentResult {
        val superResult = super<ChangeMemberRoleAction>.authorize(clan, player, arguments)

        if (!superResult.isSuccess) {
            return superResult
        }

        val target = arguments.target
        val selfMember = clan.getMember(player)
            ?: return ComponentResult.SelfNotClanMember(clan, player.uuid)

        if (player == target) {
            return ClanMemberSetRoleResult.CannotChangeOwnRole(clan)
        }

        val otherMember = arguments.targetMember

        if (otherMember >= selfMember) {
            return ClanMemberSetRoleResult.OtherSameOrHigherRole(
                clan,
                selfMember,
                otherMember
            )
        }

        return ComponentResult.EmptySuccess
    }

    override suspend fun action(
        clan: Clan,
        player: ClanPlayer,
        arguments: ChangeMemberRoleArguments
    ) = playerManager.setMemberRole(clan, arguments.targetMember, arguments.newRole, player)
}