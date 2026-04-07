package dev.slne.surf.clan.microservice.db.table

import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.dao.id.ULongIdTable

object ClanPlayerTable : ULongIdTable("clan_players") {
    val uuid = nativeUuid("uuid").uniqueIndex()
    val acceptsClanInvites = bool("accepts_clan_invites").default(true)
}