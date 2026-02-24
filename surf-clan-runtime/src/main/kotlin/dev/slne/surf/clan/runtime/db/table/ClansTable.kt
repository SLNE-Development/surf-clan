package dev.slne.surf.clan.runtime.db.table

import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.Column
import dev.slne.surf.database.libs.org.jetbrains.exposed.v1.core.Table
import dev.slne.surf.database.table.AuditableLongIdTable
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import java.util.*

object ClansTable : AuditableLongIdTable("clan_clans") {
    const val UUID_UQ_INDEX_NAME = "clan_clans_uuid_uq_idx"
    const val NAME_UQ_INDEX_NAME = "clan_clans_name_uq_idx"
    const val TAG_UQ_INDEX_NAME = "clan_clans_tag_uq_idx"

    val uuid = nativeUuid("uuid").uniqueIndex(UUID_UQ_INDEX_NAME).clientDefault { UUID.randomUUID() }
    val name = char("name", 255).uniqueIndex(NAME_UQ_INDEX_NAME)
    val tag = char("tag", 50).uniqueIndex(TAG_UQ_INDEX_NAME)

    val tagBackgroundColor = optionalTextColor("clan_tag_background_color")
    val tagForegroundColor = optionalTextColor("clan_tag_foreground_color")
    val tagShadowColor = integer("clan_tag_shadow_color")
        .transform(ShadowColor::shadowColor, ShadowColor::value)
        .nullable()
        .default(null)

    val description = char("description", 255).nullable().default(null)
    val createdBy = nativeUuid("created_by")
    val discordInvite = varchar("discord_invite", 255).nullable().default(null)
    val lastActivity = long("last_activity").nullable().default(null)
}

private fun Table.optionalTextColor(name: String): Column<TextColor?> = integer(name)
    .transform(TextColor::color, TextColor::value)
    .nullable()
    .default(null)
