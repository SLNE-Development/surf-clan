package dev.slne.surf.clan.microservice.db.repository.impl

import com.google.auto.service.AutoService
import dev.slne.surf.clan.core.player.ClanPlayerImpl
import dev.slne.surf.clan.microservice.db.repository.ClanPlayerRepository
import dev.slne.surf.clan.microservice.db.table.ClanPlayerTable
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.ResultRow
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.eq
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.insertReturning
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.selectAll
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.update
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.flow.singleOrNull
import java.util.*

@AutoService(ClanPlayerRepository::class)
class ClanPlayerRepositoryImpl : ClanPlayerRepository {
    override suspend fun findOrCreateByUuid(uuid: UUID): ClanPlayerImpl = suspendTransaction {
        ClanPlayerTable.selectAll()
            .where { ClanPlayerTable.uuid eq uuid }
            .singleOrNull()
            ?.let(::createClanPlayerDAO)
            ?: ClanPlayerTable.insertReturning {
                it[this.uuid] = uuid
            }.single().let(::createClanPlayerDAO)
    }

    override suspend fun changeAcceptsClanInvites(
        playerID: ULong,
        acceptsClanInvites: Boolean
    ): Boolean = suspendTransaction {
        ClanPlayerTable.update {
            it[this.acceptsClanInvites] = acceptsClanInvites
        } > 0
    }

    fun createClanPlayerDAO(row: ResultRow): ClanPlayerImpl = ClanPlayerImpl(
        ID = row[ClanPlayerTable.id].value,
        uuid = row[ClanPlayerTable.uuid],
        acceptsClanInvites = row[ClanPlayerTable.acceptsClanInvites]
    )
}