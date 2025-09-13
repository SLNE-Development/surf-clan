package dev.slne.surf.clan.server.db.entities

import dev.slne.surf.clan.core.common.clan.invite.ClanInviteCommon
import dev.slne.surf.clan.server.db.tables.ClanInvitesTable
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntity
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ClanInviteEntity(id: EntityID<Long>) : AuditableLongEntity(id, ClanInvitesTable) {
    companion object : AuditableLongEntityClass<ClanInviteEntity>(ClanInvitesTable)

    var clan by ClanEntity referencedOn ClanInvitesTable.clan
    var invited by ClanPlayerEntity referencedOn ClanInvitesTable.invited
    var invitedBy by ClanPlayerEntity referencedOn ClanInvitesTable.invitedBy

    fun toDto() = ClanInviteCommon(
        invitedUuid = invited.uuid,
        invitedByUuid = invitedBy.uuid,
        createdAt = createdAt,
        updatedAt = updatedAt
    )
}