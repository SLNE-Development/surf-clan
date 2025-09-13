package dev.slne.surf.clan.server.db.tables

import dev.slne.surf.clan.server.db.utils.textColor
import dev.slne.surf.cloud.api.server.exposed.columns.nativeUuid
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongIdTable

object ClansTable : AuditableLongIdTable("clans") {

    val uuid = nativeUuid("uuid").uniqueIndex()
    val name = varchar("name", 36).uniqueIndex()

    val tag = varchar("tag", 4).uniqueIndex()
    val tagForeground = textColor("tag_foreground_color")
    val tagShadowColor = textColor("tag_shadow_color").nullable()
    val tagBackgroundColor = textColor("tag_background_color")

    val discordInvite = varchar("discord_invite", 255).nullable()
    
    val createdBy = reference("created_by_id", ClanPlayersTable)

}