package dev.slne.surf.clan.core.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.InviteMemberAction
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberAddResult
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberInviteResult
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanActionExtraArguments
import dev.slne.surf.clan.api.common.util.ComponentResult

object InviteMemberActionCommon : InviteMemberAction {
    override val permission = ClanPermission.MEMBER_INVITE

    override suspend fun authorizeExtra(
        clan: Clan,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ): ComponentResult? {
        val target: ClanPlayer by arguments

        if (clan.isInvited(target)) {
            return ClanMemberInviteResult.AlreadyInvited(clan, player.uuid, target.uuid)
        }

        if (clan.isMember(target)) {
            return ClanMemberAddResult.AlreadyMember(clan, player.uuid, target.uuid)
        }
    }
}