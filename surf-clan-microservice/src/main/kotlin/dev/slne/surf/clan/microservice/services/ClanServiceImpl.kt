package dev.slne.surf.clan.microservice.services

import com.google.auto.service.AutoService
import dev.slne.clan.api.clan.*
import dev.slne.clan.api.clan.listener.ClanListener
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.clan.core.clan.AbstractClanView
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.clan.CoreClanService
import dev.slne.surf.clan.core.invite.ClanInviteImpl
import java.util.*

@AutoService(ClanService::class)
class ClanServiceImpl : CoreClanService {

    override fun init() = Unit

    override suspend fun invalidateCaches() {
        throw NotImplementedError()
    }

    override fun registerListener(listener: ClanListener) {
        throw NotImplementedError()
    }

    override fun unregisterListener(listener: ClanListener) {
        throw NotImplementedError()
    }

    override suspend fun findClanByPlayer(playerUuid: UUID): Clan? {
        throw NotImplementedError()
    }

    override suspend fun findClanByUuid(clanUuid: UUID): Clan? {
        throw NotImplementedError()
    }

    override suspend fun findClanByTag(tag: String): Clan? {
        throw NotImplementedError()
    }

    override suspend fun findClanByID(id: ULong): ClanImpl? {
        throw NotImplementedError()
    }

    override suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl> {
        throw NotImplementedError()
    }

    override fun validateClanNameAndTag(name: String, tag: String): ClanValidationResult {
        throw NotImplementedError()
    }

    override suspend fun createClan(properties: ClanCreateBuilder): ClanCreationResult {
        throw NotImplementedError()
    }

    override suspend fun updateDescription(clan: ClanImpl, description: String?): Boolean {
        throw NotImplementedError()
    }

    override suspend fun updateDiscordInvite(clan: ClanImpl, discordInvite: String?): Boolean {
        throw NotImplementedError()
    }

    override suspend fun updateTagColor(clan: ClanImpl, update: ClanTagColor.Update): Boolean {
        throw NotImplementedError()
    }

    override suspend fun fetchPendingInvites(clan: AbstractClanView): Set<ClanInviteImpl> {
        throw NotImplementedError()
    }

    override suspend fun invitePlayer(
        clan: ClanImpl,
        invitee: UUID,
        invitedBy: UUID
    ): ClanInviteResult {
        throw NotImplementedError()
    }

    override suspend fun revokeInvite(clan: ClanImpl, playerUuid: UUID): Boolean {
        throw NotImplementedError()
    }

    override suspend fun addMember(
        clan: ClanImpl,
        playerUuid: UUID,
        role: ClanMemberRole,
        addedBy: UUID?
    ): ClanMemberAddResult {
        throw NotImplementedError()
    }

    override suspend fun removeMember(clan: ClanImpl, playerUuid: UUID): Boolean {
        throw NotImplementedError()
    }

    override suspend fun delete(clan: ClanImpl): Boolean {
        throw NotImplementedError()
    }

    override suspend fun computeTagSuggestions(input: String, limit: Int): Collection<String> {
        throw NotImplementedError()
    }
}