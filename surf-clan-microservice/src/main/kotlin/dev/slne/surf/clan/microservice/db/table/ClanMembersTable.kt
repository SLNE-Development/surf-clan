package dev.slne.surf.clan.microservice.db.table

import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.ReferenceOption
import dev.slne.surf.database.table.AuditableLongIdTable

object ClanMembersTable : AuditableLongIdTable("clan_members") {
    val uuid = nativeUuid("uuid").uniqueIndex()
    val addedBy = nativeUuid("added_by").nullable().default(null)
    val role = enumerationByName<ClanMemberRole>("role", 16).default(ClanMemberRole.MEMBER)
    val clanId = reference(
        "clan_id",
        ClansTable,
        onDelete = ReferenceOption.CASCADE,
        onUpdate = ReferenceOption.CASCADE
    ).index()
}