package dev.slne.surf.clan.microservice.db.table

import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.ReferenceOption
import dev.slne.surf.database.table.AuditableLongIdTable

object ClanInvitesTable : AuditableLongIdTable("clan_invites") {
    val invited = nativeUuid("invited")
    val invitedBy = nativeUuid("invited_by")
    val clanId = reference(
        "clan_id",
        ClansTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).index()

    init {
        uniqueIndex(invited, clanId)
    }
}