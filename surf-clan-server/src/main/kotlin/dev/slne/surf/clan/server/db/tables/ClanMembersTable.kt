package dev.slne.surf.clan.server.db.tables

import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongIdTable
import org.jetbrains.exposed.sql.ReferenceOption

object ClanMembersTable : AuditableLongIdTable("clan_members") {

    val player = reference(
        "clan_player_id",
        ClanPlayersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    val addedBy = reference(
        "added_by_id",
        ClanPlayersTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    val clan = reference(
        "clan_id",
        ClansTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    )

    val role = enumerationByName<ClanMemberRole>("role", 255).default(ClanMemberRole.MEMBER)
}