package dev.slne.surf.clan.api.server

import dev.slne.surf.clan.api.common.ClanApi

interface ClanServerApi : ClanApi {

    companion object : ClanServerApi by ClanApi.delegate as ClanServerApi

}