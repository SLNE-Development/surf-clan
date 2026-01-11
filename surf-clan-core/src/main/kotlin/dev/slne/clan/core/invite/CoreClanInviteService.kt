package dev.slne.clan.core.invite

import dev.slne.clan.api.invite.ClanInviteAcceptResult
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.invite.ClanInviteService
import java.util.*

interface CoreClanInviteService : ClanInviteService {

    suspend fun fetchPendingInvites(clanID: ULong): Set<ClanInviteImpl>
    suspend fun createInvite(clanID: ULong, invitee: UUID, invitedBy: UUID): ClanInviteResult
    suspend fun deleteInvite(clanID: ULong, invitee: UUID): Boolean

    suspend fun acceptInvite(invite: ClanInviteImpl): ClanInviteAcceptResult
    suspend fun revokeInvite(invite: ClanInviteImpl): Boolean

    companion object : CoreClanInviteService by ClanInviteService.instance as CoreClanInviteService
}

