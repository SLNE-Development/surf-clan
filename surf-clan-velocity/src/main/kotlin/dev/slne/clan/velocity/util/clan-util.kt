package dev.slne.clan.velocity.util

import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.core.service.clanService

val ClanInvite.clan
    get() = clanService.findClanByInvite(this) ?: error("Clan for invite $this not found")