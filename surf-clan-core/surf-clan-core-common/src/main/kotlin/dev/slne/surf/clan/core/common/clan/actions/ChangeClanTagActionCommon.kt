package dev.slne.surf.clan.core.common.clan.actions

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.ChangeClanTagAction
import dev.slne.surf.clan.api.common.clan.actions.ChangeClanTagArguments
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.stereotype.Component

@Component
class ChangeClanTagActionCommon : ClanActionCommon<ChangeClanTagArguments>(), ChangeClanTagAction {
    override val permission = ClanPermission.OPTIONS_TAG_TAG

    override suspend fun action(
        clan: Clan,
        player: ClanPlayer,
        arguments: ChangeClanTagArguments
    ) = clanManager.setTag(clan, player, arguments.newTag)
}