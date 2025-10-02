package dev.slne.surf.clan.server.db.tables

import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongIdTable

object ClanTagBlacklistTable : AuditableLongIdTable("clan_tag_blacklists") {
    val category = varchar("category", 255)
    val tag = varchar("tag", 5).uniqueIndex()
    val description = largeText("description").nullable()
}