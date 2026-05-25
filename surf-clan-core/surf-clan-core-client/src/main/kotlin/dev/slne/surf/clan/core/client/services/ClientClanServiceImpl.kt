package dev.slne.surf.clan.core.client.services

import com.github.benmanes.caffeine.cache.Caffeine
import com.google.auto.service.AutoService
import com.sksamuel.aedile.core.asLoadingCache
import com.sksamuel.aedile.core.expireAfterWrite
import dev.slne.clan.api.clan.*
import dev.slne.clan.api.clan.listener.*
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.api.core.util.logger
import dev.slne.surf.api.core.util.mutableObjectSetOf
import dev.slne.surf.api.core.util.toObjectSet
import dev.slne.surf.clan.core.clan.AbstractClanView
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.clan.ClanTagRules
import dev.slne.surf.clan.core.clan.CoreClanService
import dev.slne.surf.clan.core.client.redis.RedisService
import dev.slne.surf.clan.core.client.rpc.clanRpcService
import dev.slne.surf.clan.core.invite.ClanInviteImpl
import dev.slne.surf.clan.core.member.ClanMemberImpl
import dev.slne.surf.redis.cache.RedisSetIndexes
import it.unimi.dsi.fastutil.chars.Char2BooleanOpenHashMap
import java.util.*
import java.util.concurrent.CopyOnWriteArrayList
import kotlin.time.Duration.Companion.minutes

@AutoService(ClanService::class)
class ClientClanServiceImpl : CoreClanService {
    private val cache = RedisService.get().redisApi.createSimpleSetRedisCache(
        RedisService.namespaced("clan_cache"),
        ClanImpl.serializer(),
        15.minutes,
        { it.clanID.toString() },
        CacheIndexes
    )

    private val tagSuggestionBucketCache = Caffeine.newBuilder()
        .maximumSize(5_000)
        .expireAfterWrite(1.minutes)
        .asLoadingCache<String, List<String>> { bucketKey ->
            clanRpcService.findClanTagsByPrefix(bucketKey, 100)
        }

    private val listeners = CopyOnWriteArrayList<ClanListener>()

    override fun init() = Unit

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

    override suspend fun invalidateCaches() {
        cache.invalidateAll()
    }

    suspend fun invalidateCachedClanByMember(member: UUID) {
        cache.removeByIndex(CacheIndexes.members, member)
    }

    suspend fun invalidateCachedClanByID(id: ULong) {
        cache.removeById(id.toString())
    }

    override suspend fun findClanByPlayer(playerUuid: UUID): Clan? {
        return cache.findCachedByIndexOrLoadNullable(CacheIndexes.members, playerUuid) {
            clanRpcService.findClanByMember(playerUuid)
        }
    }

    override suspend fun findClanByUuid(clanUuid: UUID): Clan? {
        return cache.findCachedByIndexOrLoadNullable(CacheIndexes.uuid, clanUuid) {
            clanRpcService.findClanByClanUuid(clanUuid)
        }
    }

    override suspend fun findClanByTag(tag: String): Clan? {
        return cache.findCachedByIndexOrLoadNullable(CacheIndexes.tag, tag) {
            clanRpcService.findClanByTag(normalizeTag(tag))
        }
    }

    override suspend fun findClanByID(id: ULong): ClanImpl? {
        return cache.findCachedByIndexOrLoadNullable(CacheIndexes.id, id) {
            clanRpcService.findClanById(id)
        }
    }

    override suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl> {
        return clanRpcService.findAllClansWithoutMembersSortByMemberCount()
    }

    override fun validateClanNameAndTag(
        name: String,
        tag: String
    ): ClanValidationResult {
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

        val result = clanRpcService.createClan(
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

    override suspend fun updateDescription(
        clan: ClanImpl,
        description: String?
    ): Boolean {
        val updated = clanRpcService.updateClanDescription(clan.clanID, description)

        if (updated) {
            invalidateCachedClanByID(clan.clanID)
            clan.description = description
            callClanUpdatedListeners(clan)
        }

        return updated
    }

    override suspend fun updateDiscordInvite(
        clan: ClanImpl,
        discordInvite: String?
    ): Boolean {
        val updated = clanRpcService.updateClanDiscordInvite(clan.clanID, discordInvite)

        if (updated) {
            invalidateCachedClanByID(clan.clanID)
            clan.discordInvite = discordInvite
            callClanUpdatedListeners(clan)
        }

        return updated
    }

    override suspend fun updateTagColor(
        clan: ClanImpl,
        update: ClanTagColor.Update
    ): Boolean {
        val updatedTagColor = clan.clanTagColor.update(update)
        val updated = clanRpcService.updateClanTagColor(
            clan.clanID,
            updatedTagColor.foregroundColor,
            updatedTagColor.backgroundColor,
            updatedTagColor.shadowColor
        )

        if (updated) {
            invalidateCachedClanByID(clan.clanID)
            clan.clanTagColor = updatedTagColor
            callClanUpdatedListeners(clan)
        }

        return updated
    }

    override suspend fun fetchPendingInvites(clan: AbstractClanView): Set<ClanInviteImpl> {
        return ClientClanInviteServiceImpl.get().fetchPendingInvites(clan.clanID)
    }

    override suspend fun invitePlayer(
        clan: ClanImpl,
        invitee: UUID,
        invitedBy: UUID
    ): ClanInviteResult {
        return ClientClanInviteServiceImpl.get().createInvite(clan.clanID, invitee, invitedBy)
    }

    override suspend fun revokeInvite(
        clan: ClanImpl,
        playerUuid: UUID
    ): Boolean {
        return ClientClanInviteServiceImpl.get().deleteInvite(clan.clanID, playerUuid)
    }

    override suspend fun addMember(
        clan: ClanImpl,
        playerUuid: UUID,
        role: ClanMemberRole,
        addedBy: UUID?
    ): ClanMemberAddResult {
        val result =
            ClientClanMemberServiceImpl.get().addMember(clan.clanID, playerUuid, role, addedBy)
        if (result is ClanMemberAddResult.Success) {
            clan.members = clan.members.plusElement(result.member as ClanMemberImpl).toObjectSet()
            callClanMemberUpdatedListeners(clan, result.member.uuid, true)
        }

        return result
    }

    override suspend fun removeMember(
        clan: ClanImpl,
        playerUuid: UUID
    ): Boolean {
        val result = ClientClanMemberServiceImpl.get().removeMember(clan.clanID, playerUuid)
        if (result) {
            clan.members = clan.members.filterNotTo(mutableObjectSetOf()) { it.uuid == playerUuid }
            callClanMemberUpdatedListeners(clan, playerUuid, false)
        }

        return result
    }

    override suspend fun delete(clan: ClanImpl): Boolean {
        val deleted = clanRpcService.deleteClan(clan.clanID)

        if (deleted) {
            invalidateCachedClanByID(clan.clanID)
            callClanDeletedListeners(clan)
        }

        return deleted
    }

    override suspend fun computeTagSuggestions(
        input: String,
        limit: Int
    ): Collection<String> {
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
        val id by indexOne(valueOf = { it.clanID })
        val tag by indexOne(normalize = { normalizeTag(it) }, valueOf = { it.tag })
        val uuid by indexOne(valueOf = { it.uuid })
        val members by index(valuesOf = { clan -> clan.members.map { it.uuid } })
    }

    companion object {
        private val log = logger()

        fun get() = ClanService.INSTANCE as ClientClanServiceImpl
        private fun normalizeTag(tag: String) = tag.trim().uppercase()
    }
}