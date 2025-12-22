package dev.slne.surf.clan.fallback

import com.google.auto.service.AutoService
import dev.slne.clan.core.DatabaseLoader
import dev.slne.surf.clan.fallback.table.ClanInvitesTable
import dev.slne.surf.clan.fallback.table.ClanMembersTable
import dev.slne.surf.clan.fallback.table.ClanPlayerTable
import dev.slne.surf.clan.fallback.table.ClansTable
import dev.slne.surf.database.DatabaseManager
import dev.slne.surf.database.database.DatabaseProvider
import net.kyori.adventure.util.Services
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import java.nio.file.Path

@AutoService(DatabaseLoader::class)
class DatabaseLoaderImpl : DatabaseLoader, Services.Fallback {
    lateinit var databaseProvider: DatabaseProvider
    override fun connect(path: Path) {
        databaseProvider = DatabaseManager(path, path).databaseProvider

        databaseProvider.connect()
    }

    override fun createTables() {
        transaction {
            SchemaUtils.create(
                ClansTable,
                ClanMembersTable,
                ClanPlayerTable,
                ClanInvitesTable
            )
        }
    }

    override fun disconnect() {
        if (this::databaseProvider.isInitialized) {
            databaseProvider.disconnect()
        }
    }
}