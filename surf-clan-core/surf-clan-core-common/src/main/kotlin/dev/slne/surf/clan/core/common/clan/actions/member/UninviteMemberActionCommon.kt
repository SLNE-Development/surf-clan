package dev.slne.surf.clan.core.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.UninviteMemberAction
import dev.slne.surf.clan.api.common.clan.actions.member.UninviteMemberArguments
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.stereotype.Component

@Component
class UninviteMemberActionCommon : ClanActionCommon<UninviteMemberArguments>(),
    UninviteMemberAction {
    override val permission = ClanPermission.MEMBER_INVITE

    override suspend fun action(
        clan: Clan,
        player: ClanPlayer,
        arguments: UninviteMemberArguments
    ): ComponentResult {
        val target = arguments.target

        if (!clan.isInvited(target)) {
            return ClanMemberUninviteResult.NotInvited(clan, player.uuid, target.uuid)
        }

        // FIXME: 21.09.2025 21:10 more checks needed

        return clanManager.uninviteMember(clan, player, target)
    }
}