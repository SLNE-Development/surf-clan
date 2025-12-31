package dev.slne.surf.clan.fallback.table

import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.ReferenceOption
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object ClanInvitesTable : LongIdTable("clan_invites") {
    val invited =
        varchar("invited", 36).uniqueIndex().transform({ UUID.fromString(it) }, { it.toString() })
    val invitedBy = varchar("invited_by", 36).transform({ UUID.fromString(it) }, { it.toString() })
    val clanId = long("clan_id").references(ClansTable.id, ReferenceOption.CASCADE)
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
}