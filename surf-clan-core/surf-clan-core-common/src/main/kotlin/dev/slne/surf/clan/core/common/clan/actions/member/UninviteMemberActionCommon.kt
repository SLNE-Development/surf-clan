package dev.slne.surf.clan.core.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.UninviteMemberAction
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanActionExtraArguments
import dev.slne.surf.clan.api.common.util.ComponentResult

object UninviteMemberActionCommon : UninviteMemberAction {
    override val permission = ClanPermission.MEMBER_INVITE

    override suspend fun authorizeExtra(
        clan: Clan,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ): ComponentResult? {
        val target: ClanPlayer by arguments

        if (!clan.isInvited(target)) {
            return ClanMemberUninviteResult.NotInvited(clan, player.uuid, target.uuid)
        }
    }
}