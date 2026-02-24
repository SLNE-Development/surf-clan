package dev.slne.clan.core.clan

import dev.slne.clan.api.clan.ClanService
import dev.slne.clan.api.clan.ClanTagColor
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.invite.ClanInviteImpl
import net.kyori.adventure.text.format.TextColor
import java.util.*

interface CoreClanService : ClanService {

    fun init()

    suspend fun invalidateCaches()

    suspend fun findClanByID(id: ULong): ClanImpl?

    suspend fun fetchAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl>

    suspend fun updateDescription(clan: ClanImpl, description: String?): Boolean
    suspend fun updateDiscordInvite(clan: ClanImpl, discordInvite: String?): Boolean
    suspend fun updateTagColor(clan: ClanImpl, update: ClanTagColor.Update): Boolean

    suspend fun fetchPendingInvites(clan: AbstractClanView): Set<ClanInviteImpl>
    suspend fun invitePlayer(clan: ClanImpl, invitee: UUID, invitedBy: UUID): ClanInviteResult
    suspend fun revokeInvite(clan: ClanImpl, playerUuid: UUID): Boolean

    suspend fun addMember(clan: ClanImpl, playerUuid: UUID, role: ClanMemberRole, addedBy: UUID?): ClanMemberAddResult
    suspend fun removeMember(clan: ClanImpl, playerUuid: UUID): Boolean

    suspend fun delete(clan: ClanImpl): Boolean

    suspend fun updateLastActivity(clan: ClanImpl): Boolean
    suspend fun disbandInactiveClans(inactivityDays: Int): Int

    suspend fun computeTagSuggestions(input: String, limit: Int = 100): Collection<String>

    companion object : CoreClanService by ClanService.Companion.instance as CoreClanService
}