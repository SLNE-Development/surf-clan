package dev.slne.surf.clan.runtime.db.repository.impl

import com.google.auto.service.AutoService
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.core.invite.ClanInviteImpl
import dev.slne.surf.clan.runtime.db.repository.ClanInviteRepository
import dev.slne.surf.clan.runtime.db.table.ClanInvitesTable
import dev.slne.surf.clan.runtime.db.table.ClanMembersTable
import dev.slne.surf.clan.runtime.db.table.ClanPlayerTable
import dev.slne.surf.clan.runtime.db.table.ClansTable
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.ResultRow
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.and
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.eq
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.inSubQuery
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.*
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.database.utils.asDataIntegrityViolation
import kotlinx.coroutines.flow.*
import java.util.*

@AutoService(ClanInviteRepository::class)
class ClanInviteRepositoryImpl : ClanInviteRepository {
    override suspend fun fetchPendingInvites(clanID: ULong): Set<ClanInviteImpl> = suspendTransaction {
        ClanInvitesTable
            .selectAll()
            .where { ClanInvitesTable.clanId eq clanID }
            .map(::createClanInviteDAO)
            .toSet()
    }

    override suspend fun getPendingInviteByPlayerAndClanName(
        invited: UUID,
        clanName: String
    ): ClanInviteImpl? = suspendTransaction {
        val clanIDSubQuery = ClansTable
            .select(ClansTable.id)
            .where { ClansTable.name eq clanName }

        ClanInvitesTable
            .selectAll()
            .where { ClanInvitesTable.invited eq invited and (ClanInvitesTable.clanId inSubQuery clanIDSubQuery) }
            .singleOrNull()
            ?.let(::createClanInviteDAO)
    }

    override suspend fun getPendingInvitesByPlayer(invited: UUID): List<ClanInviteImpl> = suspendTransaction {
        ClanInvitesTable
            .selectAll()
            .where { ClanInvitesTable.invited eq invited }
            .map(::createClanInviteDAO)
            .toList()
    }

    override suspend fun createInvite(
        clanID: ULong,
        invitee: UUID,
        invitedBy: UUID
    ): ClanInviteResult = suspendTransaction {
        if (isAlreadyInClan(invitee)) return@suspendTransaction ClanInviteResult.AlreadyInClan

        val invitesEnabled = ClanPlayerTable
            .select(ClanPlayerTable.acceptsClanInvites)
            .where { ClanPlayerTable.uuid eq invitee }
            .limit(1)
            .singleOrNull()
            ?.get(ClanPlayerTable.acceptsClanInvites)
            ?: true

        if (!invitesEnabled) return@suspendTransaction ClanInviteResult.InvitationsDisabled

        try {
            val row = ClanInvitesTable.insertReturning {
                it[this.invited] = invitee
                it[this.invitedBy] = invitedBy
                it[this.clanId] = clanID
            }.single()

            ClanInviteResult.Success(createClanInviteDAO(row))
        } catch (e: ExposedR2dbcException) {
            e.asDataIntegrityViolation()
            ClanInviteResult.AlreadyInvited
        }
    }

    override suspend fun deleteInvite(clanID: ULong, invitee: UUID): Boolean = suspendTransaction {
        ClanInvitesTable.deleteWhere(limit = 1) {
            ClanInvitesTable.clanId eq clanID and (ClanInvitesTable.invited eq invitee)
        } > 0
    }

    override suspend fun acceptInvite(inviteID: ULong, invitee: UUID, invitedBy: UUID): Boolean = suspendTransaction {
        if (isAlreadyInClan(invitee)) return@suspendTransaction false

        val deletedRow = ClanInvitesTable.deleteReturning {
            ClanInvitesTable.id eq inviteID and (ClanInvitesTable.invited eq invitee)
        }.singleOrNull()

        if (deletedRow == null) return@suspendTransaction false

        ClanMembersTable.insert {
            it[uuid] = invitee
            it[addedBy] = invitedBy
            it[clanId] = deletedRow[ClanInvitesTable.clanId]
        }

        true
    }

    private suspend fun isAlreadyInClan(invitee: UUID): Boolean = ClanMembersTable
        .select(ClanMembersTable.id)
        .where { ClanMembersTable.uuid eq invitee }
        .limit(1)
        .singleOrNull() != null


    fun createClanInviteDAO(row: ResultRow): ClanInviteImpl = ClanInviteImpl(
        id = row[ClanInvitesTable.id].value,
        clanID = row[ClanInvitesTable.clanId].value,
        invited = row[ClanInvitesTable.invited],
        invitedBy = row[ClanInvitesTable.invitedBy],
        createdAt = row[ClanInvitesTable.createdAt],
        updatedAt = row[ClanInvitesTable.updatedAt]
    )
}