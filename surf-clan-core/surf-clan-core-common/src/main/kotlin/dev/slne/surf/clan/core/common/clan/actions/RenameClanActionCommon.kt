package dev.slne.surf.clan.core.common.clan.actions

import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MAX_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MIN_LENGTH
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.RenameClanAction
import dev.slne.surf.clan.api.common.clan.actions.RenameClanArguments
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.result.ClanSetNameResult
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.utils.ClanActionCommon
import org.springframework.stereotype.Component

@Component
class RenameClanActionCommon : ClanActionCommon<RenameClanArguments>(), RenameClanAction {
    override val permission = ClanPermission.OPTIONS_NAME

    override suspend fun action(
        clan: Clan,
        player: ClanPlayer,
        arguments: RenameClanArguments
    ): ComponentResult {
        val oldName = clan.name
        val newName = arguments.newName

        val equalsName = oldName.equals(newName, ignoreCase = true)
        val equalsExistingClan = clanManager.clans.any {
            it.name.equals(newName, ignoreCase = true)
        }

        if (equalsName || equalsExistingClan) {
            return ClanSetNameResult.NameAlreadyInUse(newName)
        }

        if (newName.length !in CLAN_NAME_MIN_LENGTH..CLAN_NAME_MAX_LENGTH) {
            return ClanSetNameResult.NameDoesntMatchLength(
                newName,
                CLAN_NAME_MIN_LENGTH,
                CLAN_NAME_MAX_LENGTH
            )
        }

        return clanManager.setName(clan, player, newName)
    }
}