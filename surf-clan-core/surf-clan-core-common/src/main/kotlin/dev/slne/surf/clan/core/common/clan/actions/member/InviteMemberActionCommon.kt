package dev.slne.surf.clan.core.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.InviteMemberAction
import dev.slne.surf.clan.api.common.clan.actions.member.InviteMemberArguments
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberAddResult
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberInviteResult
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.stereotype.Component

@Component
class InviteMemberActionCommon : ClanActionCommon<InviteMemberArguments>(), InviteMemberAction {
    override val permission = ClanPermission.MEMBER_INVITE

    override suspend fun action(
        clan: Clan,
        player: ClanPlayer,
        arguments: InviteMemberArguments
    ): ComponentResult {
        val target = arguments.target

        if (clan.isInvited(target)) {
            return ClanMemberInviteResult.AlreadyInvited(clan, player.uuid, target.uuid)
        }

        if (clan.isMember(target)) {
            return ClanMemberAddResult.AlreadyMember(clan, player.uuid, target.uuid)
        }

        return clanManager.inviteMember(clan, player, target)
    }
}