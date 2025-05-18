package dev.slne.surf.clan.standalone

import dev.slne.surf.clan.api.common.Clan
import dev.slne.surf.clan.api.common.results.ClanCreateResult
import dev.slne.surf.clan.api.server.ClanServerApi
import dev.slne.surf.clan.core.common.internal.ClanApiCommonImpl
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClanServerApiImpl(
    private val clanService: ClanService,
) : ClanApiCommonImpl(), ClanServerApi {

    override suspend fun findClanByName(name: String) = clanService.findClanByName(name)

    override suspend fun createClan(
        name: String,
        tag: String,
        createdBy: UUID
    ): Pair<ClanCreateResult, Clan?> = clanService.createClan(name, tag, createdBy)
}