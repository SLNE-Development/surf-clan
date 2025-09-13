package dev.slne.surf.clan.server.db.tables

import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ClanInvitesTable : AuditableLongIdTable("clan_invites") {

    val clan = reference(
        "clan_id",
        ClansTable,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE
    )

    val invited = reference(
        "invited_id",
        ClanPlayersTable,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE
    )

    val invitedBy = reference(
        "invited_by_id",
        ClanPlayersTable,
        onUpdate = ReferenceOption.CASCADE,
        onDelete = ReferenceOption.CASCADE
    )

}