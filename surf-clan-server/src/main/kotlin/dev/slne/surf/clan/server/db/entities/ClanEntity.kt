package dev.slne.surf.clan.server.db.entities

import dev.slne.surf.clan.api.common.clan.invite.ClanInvite
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.core.common.clan.ClanCommon
import dev.slne.surf.clan.server.db.tables.ClanInvitesTable
import dev.slne.surf.clan.server.db.tables.ClanMembersTable
import dev.slne.surf.clan.server.db.tables.ClansTable
import dev.slne.surf.cloud.api.common.util.toObjectSet
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntity
import dev.slne.surf.cloud.api.server.exposed.table.AuditableLongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ClanEntity(id: EntityID<Long>) : AuditableLongEntity(id, ClansTable) {
    companion object : AuditableLongEntityClass<ClanEntity>(ClansTable)

    var uuid by ClansTable.uuid
    var name by ClansTable.name

    private var tag by ClansTable.tag
    private var tagForeground by ClansTable.tagForeground
    private var tagShadowColor by ClansTable.tagShadowColor
    private var tagBackgroundColor by ClansTable.tagBackgroundColor

    var clanTag: ClanTag
        get() = ClanTag(tag, tagForeground, tagShadowColor, tagBackgroundColor)
        set(value) {
            tag = value.tag
            tagForeground = value.foregroundColor
            tagShadowColor = value.shadowColor
            tagBackgroundColor = value.backgroundColor
        }

    var discordInvite by ClansTable.discordInvite

    val createdBy by ClanPlayerEntity referencedOn ClansTable.createdBy
    val members by ClanMemberEntity referrersOn ClanMembersTable.clan
    val invites by ClanInviteEntity referrersOn ClanInvitesTable.clan

    fun toDto(): ClanCommon {
        val members1 = members.map { it.toDto() }.toObjectSet()
        val invites1 = invites.map { it.toDto() }.toObjectSet()

        return ClanCommon(
            uuid = uuid,
            name = name,
            fullTag = clanTag,
            createdByUuid = createdBy.uuid,
            discordInvite = discordInvite,
            _members = members1,
            _invites = invites1,
            createdAt = createdAt,
            updatedAt = updatedAt
        )
    }
}