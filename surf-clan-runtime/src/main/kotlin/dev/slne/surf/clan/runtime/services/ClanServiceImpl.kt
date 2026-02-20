package dev.slne.surf.clan.runtime.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.expireAfterWrite
import dev.slne.clan.api.clan.*
import dev.slne.clan.api.clan.listener.*
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.clan.AbstractClanView
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.clan.ClanTagRules
import dev.slne.clan.core.clan.CoreClanService
import dev.slne.clan.core.invite.ClanInviteImpl
import dev.slne.clan.core.member.ClanMemberImpl
import dev.slne.clan.core.redis.RedisService
import dev.slne.surf.clan.runtime.db.repository.ClanRepository
import dev.slne.surf.redis.cache.RedisSetIndexes
import dev.slne.surf.surfapi.core.api.util.logger
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.chars.Char2BooleanOpenHashMap
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.time.Duration.Companion.minutes

@AutoService(ClanService::class)
class ClanServiceImpl : CoreClanService {
    private val cache = RedisService.get().redisApi.createSimpleSetRedisCache(
        RedisService.namespaced("clan_cache"),
        ClanImpl.serializer(),
        15.minutes,
        { it.id.toString() },
        CacheIndexes
    )

    private val tagSuggestionBucketCache = Caffeine.newBuilder()
        .maximumSize(5_000)
        .expireAfterWrite(1.minutes)
        .asLoadingCache<String, List<String>> { bucketKey ->
            ClanRepository.suggestTagsByPrefix(bucketKey, 100)
        }

    private val listeners = CopyOnWriteArrayList<ClanListener>()

    override fun init() = Unit

    override suspend fun invalidateCaches() {
        cache.invalidateAll()
    }

    suspend fun invalidateCachedClanByMember(member: UUID) {
        cache.removeByIndex(CacheIndexes.members, member)
    }

    suspend fun invalidateCachedClanByID(id: ULong) {
        cache.removeById(id.toString())
    }

    override fun registerListener(listener: ClanListener) {
        listeners.add(listener)
    }

    override fun unregisterListener(listener: ClanListener) {
        listeners.remove(listener)
    }

    private inline fun <reified T : ClanListener> getListeners(): List<T> {
        return listeners.filterIsInstance<T>()
    }

    private inline fun invokeListenerSafe(block: () -> Unit) {
        try {
            block()
        } catch (e: Throwable) {
            log.atWarning()
                .withCause(e)
                .log("Failed to invoke clan listener")
        }
    }

    private inline fun <reified T : ClanListener> callClanListeners(call: (T) -> Unit) {
        for (listener in getListeners<T>()) {
            invokeListenerSafe { call(listener) }
        }
    }

    fun callClanCreatedListeners(clan: Clan) {
        callClanListeners<ClanCreatedListener> { it.onClanCreated(clan) }
    }

    fun callClanDeletedListeners(clan: Clan) {
        val view = clan.view()
        callClanListeners<ClanDeletedListener> { it.onClanDeleted(view) }
    }

    fun callClanUpdatedListeners(clan: Clan) {
        callClanListeners<ClanUpdatedListener> { it.onClanUpdated(clan) }
    }

    fun callClanMemberUpdatedListeners(clan: Clan, memberUuid: UUID, added: Boolean) {
        callClanListeners<ClanUpdateMemberListener> {
            it.onClanMemberUpdated(
                clan,
                memberUuid,
                added
            )
        }
    }

    override suspend fun findClanByPlayer(playerUuid: UUID): Clan? {
        return cache.findCachedByIndexOrLoadNullable(CacheIndexes.members, playerUuid) {
            ClanRepository.findClanByPlayer(playerUuid)
        }
    }

    override suspend fun findClanByUuid(clanUuid: UUID): Clan? {
        return cache.findCachedByIndexOrLoadNullable(CacheIndexes.uuid, clanUuid) {
            ClanRepository.findClanByUuid(clanUuid)
        }
    }

    override suspend fun findClanByTag(tag: String): Clan? {
        return cache.findCachedByIndexOrLoadNullable(CacheIndexes.tag, tag) {
            ClanRepository.findClanByTag(normalizeTag(tag))
        }
    }

    override suspend fun findClanByID(id: ULong): ClanImpl? {
        return cache.findCachedByIndexOrLoadNullable(CacheIndexes.id, id) {
            ClanRepository.findClanByID(id)
        }
    }

    override suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl> {
        return ClanRepository.fetchAllClansWithoutMembersSortByMemberCount()
    }

    override fun validateClanNameAndTag(name: String, tag: String): ClanValidationResult {
        if (name.length < Clan.MIN_NAME_LENGTH || name.length > Clan.MAX_NAME_LENGTH) {
            return ClanValidationResult.NameOutOfRange(Clan.MIN_NAME_LENGTH, Clan.MAX_NAME_LENGTH)
        }

        if (tag.length < Clan.MIN_TAG_LENGTH || tag.length > Clan.MAX_TAG_LENGTH) {
            return ClanValidationResult.TagOutOfRange(Clan.MIN_TAG_LENGTH, Clan.MAX_TAG_LENGTH)
        }

        val charValidations = Char2BooleanOpenHashMap(tag.length)
        for (char in tag) {
            charValidations[char] = char.isLetterOrDigit()
        }
        if (charValidations.values.any { it == false }) {
            return ClanValidationResult.InvalidTagCharacters(charValidations)
        }

        if (!ClanTagRules.isValid(tag)) {
            return ClanValidationResult.TagViolation
        }

        return ClanValidationResult.Valid
    }

    override suspend fun createClan(properties: ClanCreateBuilder): ClanCreationResult {
        val nameTagValidation = validateClanNameAndTag(properties.name, properties.tag)
        if (nameTagValidation != ClanValidationResult.Valid) {
            return ClanCreationResult.InvalidTagOrName(nameTagValidation)
        }

        val result = ClanRepository.create(
            properties.name,
            normalizeTag(properties.tag),
            properties.owner,
            properties.tagColor?.foregroundColor,
            properties.tagColor?.backgroundColor,
            properties.tagColor?.shadowColor,
            properties.description,
            properties.discordInvite
        )

        if (result is ClanCreationResult.Success) {
            callClanCreatedListeners(result.clan)
        }

        return result
    }

    override suspend fun updateDescription(clan: ClanImpl, description: String?): Boolean {
        val updated = ClanRepository.updateDescription(clan.id, description)
        if (updated) {
            invalidateCachedClanByID(clan.id)
            clan.description = description
            callClanUpdatedListeners(clan)
        }

        return updated
    }

    override suspend fun updateDiscordInvite(clan: ClanImpl, discordInvite: String?): Boolean {
        val updated = ClanRepository.updateDiscordInvite(clan.id, discordInvite)
        if (updated) {
            invalidateCachedClanByID(clan.id)
            clan.discordInvite = discordInvite
            callClanUpdatedListeners(clan)
        }

        return updated
    }

    override suspend fun updateTagColor(clan: ClanImpl, update: ClanTagColor.Update): Boolean {
        val updatedTagColor = clan.getClanTagColorOrDefault().applyUpdate(update)

        val updated = ClanRepository.updateTagColor(
            clan.id,
            updatedTagColor.foregroundColor,
            updatedTagColor.backgroundColor,
            updatedTagColor.shadowColor
        )

        if (updated) {
            invalidateCachedClanByID(clan.id)
            clan.clanTagColor = updatedTagColor
            callClanUpdatedListeners(clan)
        }

        return updated
    }

    override suspend fun fetchPendingInvites(clan: AbstractClanView): Set<ClanInviteImpl> {
        return ClanInviteServiceImpl.get().fetchPendingInvites(clan.id)
    }

    override suspend fun invitePlayer(
        clan: ClanImpl,
        invitee: UUID,
        invitedBy: UUID
    ): ClanInviteResult {
        return ClanInviteServiceImpl.get().createInvite(clan.id, invitee, invitedBy)
    }

    override suspend fun revokeInvite(clan: ClanImpl, playerUuid: UUID): Boolean {
        return ClanInviteServiceImpl.get().deleteInvite(clan.id, playerUuid)
    }

    override suspend fun addMember(
        clan: ClanImpl,
        playerUuid: UUID,
        role: ClanMemberRole,
        addedBy: UUID?
    ): ClanMemberAddResult {
        val result = ClanMemberServiceImpl.get().addMember(clan.id, playerUuid, role, addedBy)
        if (result is ClanMemberAddResult.Success) {
            clan.members = clan.members.plusElement(result.member as ClanMemberImpl).toObjectSet()
            callClanMemberUpdatedListeners(clan, result.member.uuid, true)
        }

        return result
    }

    override suspend fun removeMember(clan: ClanImpl, playerUuid: UUID): Boolean {
        val result = ClanMemberServiceImpl.get().removeMember(clan.id, playerUuid)
        if (result) {
            clan.members = clan.members.filterNotTo(mutableObjectSetOf()) { it.uuid == playerUuid }
            callClanMemberUpdatedListeners(clan, playerUuid, false)
        }

        return result
    }

    override suspend fun delete(clan: ClanImpl): Boolean {
        val deleted = ClanRepository.delete(clan.id)
        if (deleted) {
            invalidateCachedClanByID(clan.id)
            callClanDeletedListeners(clan)
        }

        return deleted
    }

    override suspend fun computeTagSuggestions(input: String, limit: Int): Collection<String> {
        val normalized = normalizeTag(input)
        if (normalized.isEmpty()) {
            return tagSuggestionBucketCache.underlying()
                .asMap()
                .values
                .flatMap { it.getNow(emptyList()) }
                .distinct()
                .take(limit)
        }

        val cappedLimit = limit.coerceIn(1, 100)
        val bucketKey = normalized.take(minOf(3, normalized.length))
        val bucketTags = tagSuggestionBucketCache.get(bucketKey)

        return bucketTags
            .asSequence()
            .filter { it.startsWith(normalized) }
            .take(cappedLimit)
            .toList()
    }

    object CacheIndexes : RedisSetIndexes<ClanImpl>() {
        val id by indexOne(valueOf = { it.id })
        val tag by indexOne(normalize = { normalizeTag(it) }, valueOf = { it.tag })
        val uuid by indexOne(valueOf = { it.uuid })
        val members by index(valuesOf = { clan -> clan.members.map { it.uuid } })
    }

    companion object {
        private val log = logger()
        fun get() = ClanService.instance as ClanServiceImpl

        private fun normalizeTag(tag: String) = tag.trim().uppercase()
    }
}