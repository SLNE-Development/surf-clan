package dev.slne.clan.api

import com.velocitypowered.api.proxy.Player
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.util.requiredService
import it.unimi.dsi.fastutil.objects.ObjectSet

val surfClanApi = requiredService<SurfClanApi>()

interface SurfClanApi {
    fun findClan(player: Player): Clan?
    suspend fun findClanPlayer(player: Player): ClanPlayer

    fun getWhitelistedClans(): ObjectSet<String>
}