package dev.slne.surf.clan.standalone

import dev.slne.surf.clan.api.common.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.results.ClanCreateResult
import dev.slne.surf.clan.api.server.ClanServerApi
import dev.slne.surf.clan.core.common.internal.ClanApiCommonImpl
import org.springframework.stereotype.Component

@Component
class ClanServerApiImpl(
    private val clanService: ClanService,
) : ClanApiCommonImpl(), ClanServerApi {

    override suspend fun findClanByName(name: String) = clanService.findClanByName(name)

    override suspend fun createClan(
        name: String,
        tag: String,
        createdBy: ClanPlayer
    ): Pair<ClanCreateResult, Clan?> = clanService.createClan(name, tag, createdBy)

    override suspend fun findClanByTag(tag: String) = clanService.findClanByTag(tag)

    override suspend fun findClanByPlayer(player: ClanPlayer) =
        clanService.findClanByPlayer(player)
}