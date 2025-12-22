package dev.slne.surf.clan.fallback.table

import dev.slne.clan.api.member.ClanMemberRole
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime

object ClanMembersTable : LongIdTable("clan_members") {
    val uuid = varchar("uuid", 36).uniqueIndex()
        .transform({ java.util.UUID.fromString(it) }, { it.toString() })
    val addedBy =
        varchar("added_by", 36).transform({ java.util.UUID.fromString(it) }, { it.toString() })
    val role = enumeration<ClanMemberRole>("role")
    val clanId = long("clan_id").references(ClansTable.id)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
}