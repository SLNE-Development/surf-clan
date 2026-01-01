package dev.slne.clan.api

data class ClanModificationListener(
    val action: (Clan) -> Unit
)