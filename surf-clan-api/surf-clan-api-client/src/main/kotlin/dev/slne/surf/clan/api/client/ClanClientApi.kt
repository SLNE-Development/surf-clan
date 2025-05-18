package dev.slne.surf.clan.api.client

import dev.slne.surf.clan.api.common.ClanApi

interface ClanClientApi : ClanApi {

    companion object : ClanClientApi by ClanApi.delegate as ClanClientApi

}