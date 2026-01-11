package dev.slne.surf.clan.runtime.db.table

import dev.slne.clan.api.clan.Clan
import dev.slne.surf.database.columns.nativeUuid
import dev.slne.surf.database.table.AuditableLongIdTable
import net.kyori.adventure.text.format.TextColor
import java.util.*

object ClansTable : AuditableLongIdTable("clan_clans") {
    const val UUID_UQ_INDEX_NAME = "clan_clans_uuid_uq_idx"
    const val NAME_UQ_INDEX_NAME = "clan_clans_name_uq_idx"
    const val TAG_UQ_INDEX_NAME = "clan_clans_tag_uq_idx"

    val uuid = nativeUuid("uuid").uniqueIndex(UUID_UQ_INDEX_NAME).clientDefault { UUID.randomUUID() }
    val name = char("name", 255).uniqueIndex(NAME_UQ_INDEX_NAME)
    val tag = char("tag", 50).uniqueIndex(TAG_UQ_INDEX_NAME)
    val tagColor = integer("clan_tag_color")
        .transform(TextColor::color, TextColor::value)
        .clientDefault { Clan.DEFAULT_CLAN_TAG_COLOR }
    val description = char("description", 255).nullable().default(null)
    val createdBy = nativeUuid("created_by")
    val discordInvite = varchar("discord_invite", 255).nullable().default(null)
}