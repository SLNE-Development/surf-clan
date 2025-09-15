package dev.slne.surf.clan.core.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.RemoveMemberAction
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberRemoveResult
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanActionExtraArguments
import dev.slne.surf.clan.api.common.util.ComponentResult

object RemoveMemberActionCommon : RemoveMemberAction {
    override val permission = ClanPermission.MEMBER_REMOVE

    override suspend fun authorizeExtra(
        clan: Clan,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ): ComponentResult? {
        val target: ClanPlayer by arguments

        if (player == target) {
            return ClanMemberRemoveResult.CannotKickYourself(clan)
        }

        return ClanMemberRemoveResult.Success(clan, target.uuid)
    }
}

