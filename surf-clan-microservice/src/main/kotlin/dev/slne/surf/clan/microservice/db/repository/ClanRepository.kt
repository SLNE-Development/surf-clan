package dev.slne.surf.clan.microservice.db.repository

import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.api.clan.update.ClanNameAndTag
import dev.slne.surf.api.core.util.requiredService
import dev.slne.surf.clan.core.clan.ClanImpl
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import java.util.*

private val instance = requiredService<ClanRepository>()

interface ClanRepository {
    suspend fun findClanByPlayer(playerUuid: UUID): ClanImpl?
    suspend fun findClanByUuid(clanUuid: UUID): ClanImpl?
    suspend fun findClanByTag(tag: String): ClanImpl?
    suspend fun findClanByID(id: ULong): ClanImpl?
    suspend fun findClanByName(name: String): ClanImpl?
    suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl>

    suspend fun updateDescription(clanID: ULong, description: String?): Boolean
    suspend fun updateDiscordInvite(clanID: ULong, discordInvite: String?): Boolean
    suspend fun updateTagColor(
        clanID: ULong,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?
    ): Boolean

    suspend fun updateClanNameAndTag(
        clanID: ULong,
        name: String?,
        tag: String?
    ): ClanNameAndTag.UpdateResult

    suspend fun create(
        name: String,
        tag: String,
        owner: UUID,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?,
        description: String?,
        discordInvite: String?
    ): ClanCreationResult

    suspend fun delete(clanID: ULong): Boolean

    suspend fun suggestTagsByPrefix(prefix: String, limit: Int): List<String>

    companion object : ClanRepository by instance {
        val INSTANCE get() = instance
    }
}