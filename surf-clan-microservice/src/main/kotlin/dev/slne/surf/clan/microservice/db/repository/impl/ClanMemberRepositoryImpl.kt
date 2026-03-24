package dev.slne.surf.clan.microservice.db.repository.impl

import com.google.auto.service.AutoService
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.clan.core.member.ClanMemberImpl
import dev.slne.surf.clan.microservice.db.repository.ClanMemberRepository
import dev.slne.surf.clan.microservice.db.table.ClanMembersTable
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.ResultRow
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.and
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.eq
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.*
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.database.utils.asDataIntegrityViolation
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import java.util.*

@AutoService(ClanMemberRepository::class)
class ClanMemberRepositoryImpl : ClanMemberRepository {
    override suspend fun createMember(
        clanID: ULong,
        player: UUID,
        role: ClanMemberRole,
        invitedBy: UUID?
    ): ClanMemberAddResult = suspendTransaction {
        try {
            val row = ClanMembersTable.insertReturning {
                it[this.uuid] = player
                it[this.addedBy] = invitedBy
                it[this.role] = role
                it[this.clanId] = clanID
            }.single()

            ClanMemberAddResult.Success(createMemberDAO(row))
        } catch (e: ExposedR2dbcException) {
            e.asDataIntegrityViolation()
            ClanMemberAddResult.AlreadyMember
        }
    }

    override suspend fun deleteMember(clanID: ULong, player: UUID): Boolean = suspendTransaction {
        ClanMembersTable.deleteWhere(limit = 1) {
            ClanMembersTable.clanId eq clanID and (ClanMembersTable.uuid eq player)
        } > 0
    }

    override suspend fun changeRole(
        memberID: ULong,
        role: ClanMemberRole
    ): Boolean = suspendTransaction {
        ClanMembersTable.update({ ClanMembersTable.id eq memberID }) {
            it[this.role] = role
        } > 0
    }

    override suspend fun findByUuid(uuid: UUID): ClanMemberImpl? = suspendTransaction {
        ClanMembersTable.selectAll()
            .where { ClanMembersTable.uuid eq uuid }
            .limit(1)
            .singleOrNull()
            ?.let(::createMemberDAO)
    }

    companion object {
        fun createMemberDAO(row: ResultRow): ClanMemberImpl = ClanMemberImpl(
            ID = row[ClanMembersTable.id].value,
            uuid = row[ClanMembersTable.uuid],
            role = row[ClanMembersTable.role],
            addedBy = row[ClanMembersTable.addedBy],
            createdAt = row[ClanMembersTable.createdAt],
            updatedAt = row[ClanMembersTable.updatedAt]
        )
    }
}