package dev.slne.surf.clan.core.clan

import dev.slne.clan.api.clan.ClanService
import dev.slne.clan.api.clan.ClanTagColor
import dev.slne.clan.api.clan.update.ClanNameAndTag
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.clan.core.invite.ClanInviteImpl
import java.util.*

interface CoreClanService : ClanService {
    fun init()

    suspend fun invalidateCaches()

    suspend fun findClanByID(id: ULong): ClanImpl?

    suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl>

    suspend fun updateDescription(clan: ClanImpl, description: String?): Boolean
    suspend fun updateDiscordInvite(clan: ClanImpl, discordInvite: String?): Boolean
    suspend fun updateTagColor(clan: ClanImpl, update: ClanTagColor.Update): Boolean
    suspend fun updateClanNameAndTag(clan: ClanImpl, update: ClanNameAndTag.Update): ClanNameAndTag.UpdateResult
    suspend fun testClanNameAndTagUpdate(clan: ClanImpl, update: ClanNameAndTag.Update): ClanNameAndTag.UpdateResult

    suspend fun fetchPendingInvites(clan: AbstractClanView): Set<ClanInviteImpl>
    suspend fun invitePlayer(clan: ClanImpl, invitee: UUID, invitedBy: UUID): ClanInviteResult
    suspend fun revokeInvite(clan: ClanImpl, playerUuid: UUID): Boolean

    suspend fun addMember(
        clan: ClanImpl,
        playerUuid: UUID,
        role: ClanMemberRole,
        addedBy: UUID?
    ): ClanMemberAddResult

    suspend fun removeMember(clan: ClanImpl, playerUuid: UUID): Boolean

    suspend fun delete(clan: ClanImpl): Boolean

    suspend fun computeTagSuggestions(input: String, limit: Int = 100): Collection<String>

    companion object : CoreClanService by ClanService.INSTANCE as CoreClanService
}