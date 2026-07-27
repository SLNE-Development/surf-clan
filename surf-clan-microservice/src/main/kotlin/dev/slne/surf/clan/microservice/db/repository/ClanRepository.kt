package dev.slne.surf.clan.microservice.db.repository

import dev.slne.clan.api.clan.ClanCreationResult
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
    suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl>

    /**
     * Clans that cannot free themselves anymore: no members at all, or every member offline for
     * longer than [dev.slne.clan.api.clan.Clan.INACTIVE_AFTER].
     */
    suspend fun findDeletableClans(): List<DeletableClan>

    /**
     * Deletes those of [clanIDs] that still qualify for deletion at the moment of the statement.
     *
     * The ids are a preselection, not the decision — see the implementation. Returns how many rows
     * were actually removed, which may be fewer than [clanIDs] has entries.
     */
    suspend fun deleteClansStillDeletable(clanIDs: Collection<ULong>): Int

    suspend fun updateDescription(clanID: ULong, description: String?): Boolean
    suspend fun updateDiscordInvite(clanID: ULong, discordInvite: String?): Boolean
    suspend fun updateTagColor(
        clanID: ULong,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?
    ): Boolean

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