package dev.slne.clan.velocity.api

import com.google.auto.service.AutoService
import com.velocitypowered.api.proxy.Player
import dev.slne.clan.api.ClanModificationListener
import dev.slne.clan.api.SurfClanApi
import dev.slne.clan.api.surfClanApi
import dev.slne.clan.core.service.clanPlayerService
import dev.slne.clan.core.service.clanService
import dev.slne.clan.velocity.clanConfigHolder
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import net.kyori.adventure.text.Component
import net.kyori.adventure.util.Services
import java.util.*

@AutoService(SurfClanApi::class)
class SurfClanApiImpl : SurfClanApi, Services.Fallback {
    override fun findClan(player: Player) = clanService.findClanByMember(player.uniqueId)
    override fun findClan(playerUuid: UUID) = clanService.findClanByMember(playerUuid)
    override fun findClanByUuid(clanUuid: UUID) = clanService.findClanByUuid(clanUuid)

    override suspend fun findClanPlayer(player: Player) =
        clanPlayerService.findClanPlayerByUuid(player.uniqueId) ?: error("ClanPlayer not found")

    override fun getWhitelistedClans() = clanConfigHolder.config.whitelistedTags.toObjectSet()
    override fun renderClanTag(
        player: Player,
        minSize: Int
    ): Component {
        val clan = surfClanApi.findClan(player) ?: return Component.empty()
        val clanTag = clan.tag

        val whitelistedClanTags = surfClanApi.getWhitelistedClans()

        if ((clanTag.isEmpty() || clan.members.size < minSize) && clanTag !in whitelistedClanTags) {
            return Component.empty()
        }

        return clan.getTranslatedClanTag()
    }

    override fun renderClanTag(
        playerUuid: UUID,
        minSize: Int
    ): Component {
        val clan = surfClanApi.findClan(playerUuid) ?: return Component.empty()
        val clanTag = clan.tag

        val whitelistedClanTags = surfClanApi.getWhitelistedClans()

        if ((clanTag.isEmpty() || clan.members.size < minSize) && clanTag !in whitelistedClanTags) {
            return Component.empty()
        }

        return clan.getTranslatedClanTag()
    }

    override fun addClanModificationListener(
        listener: ClanModificationListener
    ) {
        clanService.addClanModificationListener(listener)
    }
}