package dev.slne.clan.api.clan.listener

import dev.slne.clan.api.clan.Clan

fun interface ClanCreatedListener : ClanListener {
    fun onClanCreated(clan: Clan)
}