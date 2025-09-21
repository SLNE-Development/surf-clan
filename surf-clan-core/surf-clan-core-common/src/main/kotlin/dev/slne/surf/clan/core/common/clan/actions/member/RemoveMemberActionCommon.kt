package dev.slne.surf.clan.core.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.RemoveMemberAction
import dev.slne.surf.clan.api.common.clan.actions.member.RemoveMemberArguments
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberRemoveResult
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.stereotype.Component

@Component
class RemoveMemberActionCommon : ClanActionCommon<RemoveMemberArguments>(), RemoveMemberAction {
    override val permission = ClanPermission.MEMBER_REMOVE

    override suspend fun action(
        clan: Clan,
        player: ClanPlayer,
        arguments: RemoveMemberArguments
    ): ComponentResult {
        val target = arguments.target

        if (player == target) {
            return ClanMemberRemoveResult.CannotKickYourself(clan, player.uuid)
        }

        // FIXME: 21.09.2025 21:10 More checks needed

        return clanManager.removeMember(clan, player, target)
    }
}

