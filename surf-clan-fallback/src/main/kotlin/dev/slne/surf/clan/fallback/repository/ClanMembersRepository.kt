package dev.slne.surf.clan.fallback.repository

import dev.slne.clan.api.member.ClanMember
import dev.slne.surf.clan.fallback.table.ClanMembersTable
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.sql.SqlExpressionBuilder.eq
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction

val clanMembersRepository = ClanMembersRepository()

class ClanMembersRepository {
    suspend fun findMembersByClanId(clanId: Long) = newSuspendedTransaction(Dispatchers.IO) {
        ClanMembersTable.selectAll().where(ClanMembersTable.clanId eq clanId).map {
            ClanMember(
                uuid = it[ClanMembersTable.uuid],
                role = it[ClanMembersTable.role],
                addedBy = it[ClanMembersTable.addedBy],
                createdAt = it[ClanMembersTable.createdAt],
                updatedAt = it[ClanMembersTable.updatedAt]
            )
        }
    }
}