package dev.slne.surf.clan.fallback.table

import dev.slne.surf.bitmap.bitmaps.Bitmaps
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object ClansTable : LongIdTable("clans") {
    val uuid =
        varchar("uuid", 36).uniqueIndex().transform({ UUID.fromString(it) }, { it.toString() })
    val name = varchar("name", 255).uniqueIndex()
    val tag = varchar("tag", 50).uniqueIndex()
    val color = enumeration<Bitmaps>("clan_tag_color").nullable()
    val description = varchar("description", 255).nullable()
    val createdBy = varchar("created_by", 36).transform({ UUID.fromString(it) }, { it.toString() })
    val discordInvite = varchar("discord_invite", 255).nullable()
    val createdAt = datetime("created_at")
    val updatedAt = datetime("updated_at").nullable()
}