package dev.slne.surf.clan.server.db.entities

import dev.slne.surf.clan.core.common.clan.member.ClanMemberCommon
import dev.slne.surf.clan.server.db.tables.ClanMembersTable
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntity
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ClanMemberEntity(id: EntityID<Long>) : AuditableLongEntity(id, ClanMembersTable) {
    companion object : AuditableLongEntityClass<ClanMemberEntity>(ClanMembersTable)

    var role by ClanMembersTable.role

    var player by ClanPlayerEntity referencedOn ClanMembersTable.player
    var addedBy by ClanPlayerEntity referencedOn ClanMembersTable.addedBy
    var clan by ClanEntity referencedOn ClanMembersTable.clan

    fun toDto() = ClanMemberCommon(
        uuid = player.uuid,
        role = role,
        addedByUuid = addedBy.uuid,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}