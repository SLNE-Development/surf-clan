package dev.slne.surf.clan.runtime.db

import com.google.auto.service.AutoService
import dev.slne.clan.core.ClanInstance
import dev.slne.clan.core.DatabaseService
import dev.slne.surf.clan.runtime.db.table.ClanInvitesTable
import dev.slne.surf.clan.runtime.db.table.ClanMembersTable
import dev.slne.surf.clan.runtime.db.table.ClanPlayerTable
import dev.slne.surf.clan.runtime.db.table.ClansTable
import dev.slne.surf.database.DatabaseApi
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.SchemaUtils
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.r2dbc.transactions.suspendTransaction

@AutoService(DatabaseService::class)
class DatabaseServiceImpl : DatabaseService {
    val databaseApi = DatabaseApi.create(ClanInstance.get().dataPath)

    override suspend fun createTables() = suspendTransaction {
        SchemaUtils.create(
            ClansTable,
            ClanMembersTable,
            ClanPlayerTable,
            ClanInvitesTable
        )
    }

    override fun disconnect() {
        databaseApi.shutdown()
    }
}