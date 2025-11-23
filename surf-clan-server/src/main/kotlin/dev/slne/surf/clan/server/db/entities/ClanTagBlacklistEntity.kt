package dev.slne.surf.clan.server.db.entities

import dev.slne.surf.clan.server.db.tables.ClanTagBlacklistTable
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntity
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ClanTagBlacklistEntity(id: EntityID<Long>) : AuditableLongEntity(id, ClanTagBlacklistTable) {
    companion object : AuditableLongEntityClass<ClanTagBlacklistEntity>(ClanTagBlacklistTable)

    var category by ClanTagBlacklistTable.category
    var tag by ClanTagBlacklistTable.tag
    var description by ClanTagBlacklistTable.description

    fun toDto() = ClanTagBlacklistDto(
        category = category,
        tag = tag,
        description = description
    )
}

data class ClanTagBlacklistDto(
    val category: String,
    val tag: String,
    val description: String?
)