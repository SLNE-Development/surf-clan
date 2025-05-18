package dev.slne.surf.clan.api.common

import dev.slne.surf.surfapi.core.api.util.requiredService

val clanApi get() = ClanApi.instance

interface ClanApi {

    companion object {
        val instance = requiredService<ClanApi>()
    }

}