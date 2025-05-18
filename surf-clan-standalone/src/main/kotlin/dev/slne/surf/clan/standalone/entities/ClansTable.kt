package dev.slne.surf.clan.standalone.entities

import dev.slne.surf.bitmap.bitmaps.Bitmaps
import dev.slne.surf.cloud.api.server.exposed.columns.zonedDateTime
import org.jetbrains.exposed.dao.id.LongIdTable
import java.util.*

object ClansTable : LongIdTable() {

    val uuid = varchar("uuid", 36).transform({ UUID.fromString(it) }, { it.toString() })
    val name = varchar("name", 255)
    val tag = varchar("tag", 5)
    val description = text("description").nullable()
    val clanTagColor = enumerationByName<Bitmaps>("clan_tag_color", 255)
    val createdBy = varchar("created_by", 36).transform({ UUID.fromString(it) }, { it.toString() })
    val discordInvite = varchar("discord_invite", 255).nullable()
    val createdAt = zonedDateTime("created_at").nullable()
    val updatedAt = zonedDateTime("updated_at").nullable()

}