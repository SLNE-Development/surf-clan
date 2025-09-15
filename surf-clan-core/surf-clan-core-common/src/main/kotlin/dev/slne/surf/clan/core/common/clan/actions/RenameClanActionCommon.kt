package dev.slne.surf.clan.core.common.clan.actions

import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MAX_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MIN_LENGTH
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.RenameClanAction
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.result.ClanSetNameResult
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanActionExtraArguments
import dev.slne.surf.clan.api.common.util.ComponentResult

object RenameClanActionCommon : RenameClanAction {
    override val permission = ClanPermission.OPTIONS_NAME

    override suspend fun authorizeExtra(
        clan: Clan,
        player: ClanPlayer,
        arguments: ClanActionExtraArguments
    ): ComponentResult? {
        if (clans.any { it.name.equals(name, true) }) {
            return ClanSetNameResult.NameAlreadyInUse(name)
        }

        if (name.length !in CLAN_NAME_MIN_LENGTH..CLAN_NAME_MAX_LENGTH) {
            return ClanSetNameResult.NameDoesntMatchLength(
                name,
                CLAN_NAME_MIN_LENGTH,
                CLAN_NAME_MAX_LENGTH
            )
        }
    }
}