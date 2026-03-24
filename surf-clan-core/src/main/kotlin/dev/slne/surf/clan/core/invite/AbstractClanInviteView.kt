package dev.slne.surf.clan.core.invite

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInviteView
import dev.slne.surf.clan.core.clan.CoreClanService

abstract class AbstractClanInviteView : ClanInviteView {
    abstract val clanID: ULong

    override suspend fun getClan(): Clan? {
        return CoreClanService.Companion.findClanByID(clanID)
    }

    override suspend fun getClanOrThrow(): Clan {
        return getClan() ?: throw NoSuchElementException("Clan with ID $clanID does not exist")
    }
}