package dev.slne.clan.core.invite

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInviteView
import dev.slne.clan.core.clan.CoreClanService

abstract class AbstractClanInviteView: ClanInviteView {
    abstract val clanID: ULong

    override suspend fun getClan(): Clan? {
        return CoreClanService.findClanByID(clanID)
    }

    override suspend fun getClanOrThrow(): Clan {
        return getClan() ?: error("Clan not found")
    }
}