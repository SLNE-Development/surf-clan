package dev.slne.surf.clan.fallback.table

import org.jetbrains.exposed.dao.id.LongIdTable
import java.util.*

object ClanPlayerTable : LongIdTable("clan_players") {
    val username = varchar("username", 16)
    val uuid =
        varchar("uuid", 36).uniqueIndex().transform({ UUID.fromString(it) }, { it.toString() })
    val acceptsClanInvites = bool("accepts_clan_invites").default(true)
}