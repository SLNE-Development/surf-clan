package dev.slne.surf.clan.microservice.db.table

import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.columns.time.offsetDateTime
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.Table

/**
 * Read-only view of surf-core's player registry, used to tell active from inactive clan members.
 *
 * Owned by surf-core (`dev.slne.surf.core.microservice.database.tables.SurfPlayersTable`) and living
 * in the `surf-core` schema of the shared database. Never write to it, and do not add columns beyond
 * what member activity needs — a smaller surface breaks less often when surf-core changes.
 */
object SurfPlayersTable : Table("surf-core.surf_players") {
    val uuid = nativeUuid("uuid")
    val lastSeen = offsetDateTime("last_seen").nullable()
}
