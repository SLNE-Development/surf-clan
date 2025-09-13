package dev.slne.surf.clan.server.db.tables

import dev.slne.surf.cloud.api.server.exposed.columns.nativeUuid
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongIdTable

object ClanPlayersTable : AuditableLongIdTable("clan_players") {

    val uuid = nativeUuid("uuid").uniqueIndex()
    val acceptsClanInvites = bool("accepts_clan_invites").default(true)

}