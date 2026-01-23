package dev.slne.clan.api.clan.listener

import dev.slne.clan.api.clan.ClanView

fun interface ClanDeletedListener : ClanListener {
    fun onClanDeleted(clan: ClanView)
}