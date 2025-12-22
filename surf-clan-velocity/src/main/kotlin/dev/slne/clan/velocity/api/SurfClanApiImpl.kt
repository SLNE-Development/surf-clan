package dev.slne.clan.velocity.api

import com.google.auto.service.AutoService
import com.velocitypowered.api.proxy.Player
import dev.slne.clan.api.SurfClanApi
import dev.slne.clan.core.service.clanPlayerService
import dev.slne.clan.core.service.clanService
import dev.slne.clan.velocity.clanConfigHolder
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import net.kyori.adventure.util.Services

@AutoService(SurfClanApi::class)
class SurfClanApiImpl : SurfClanApi, Services.Fallback {
    override fun findClan(player: Player) = clanService.findClanByMember(player.uniqueId)
    override suspend fun findClanPlayer(player: Player) =
        clanPlayerService.findClanPlayerByUuid(player.uniqueId) ?: error("ClanPlayer not found")

    override fun getWhitelistedClans() = clanConfigHolder.config.whitelistedTags.toObjectSet()
}