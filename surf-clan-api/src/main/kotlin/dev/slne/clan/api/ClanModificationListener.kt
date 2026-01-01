package dev.slne.clan.api

import java.util.*

data class ClanModificationListener(
    val clanUuid: UUID,
    val action: (Clan) -> Unit
)