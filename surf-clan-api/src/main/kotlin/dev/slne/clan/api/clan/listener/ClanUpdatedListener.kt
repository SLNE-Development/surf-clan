package dev.slne.clan.api.clan.listener

import dev.slne.clan.api.clan.Clan

fun interface ClanUpdatedListener : ClanListener {
    fun onClanUpdated(clan: Clan)
}