package dev.slne.surf.clan.server.db.entities

import dev.slne.surf.clan.core.common.player.ClanPlayerCommon
import dev.slne.surf.clan.server.db.tables.ClanPlayersTable
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntity
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ClanPlayerEntity(id: EntityID<Long>) : AuditableLongEntity(id, ClanPlayersTable) {
    companion object : AuditableLongEntityClass<ClanPlayerEntity>(ClanPlayersTable)

    var uuid by ClanPlayersTable.uuid
    var acceptsClanInvites by ClanPlayersTable.acceptsClanInvites

    fun toDto() = ClanPlayerCommon(
        uuid = uuid,
        acceptsClanInvites = acceptsClanInvites,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}