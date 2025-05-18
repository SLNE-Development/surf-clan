package dev.slne.surf.clan.standalone.entities

import dev.slne.surf.clan.core.common.ClanImpl
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID

class ClanEntity(id: EntityID<Long>) : LongEntity(id) {
    companion object : LongEntityClass<ClanEntity>(ClansTable)

    var uuid by ClansTable.uuid
    var name by ClansTable.name
    var tag by ClansTable.tag
    var description by ClansTable.description
    var clanTagColor by ClansTable.clanTagColor
    var createdBy by ClansTable.createdBy
    var discordInvite by ClansTable.discordInvite
    var createdAt by ClansTable.createdAt
    var updatedAt by ClansTable.updatedAt

    fun toClanImpl() = ClanImpl(
        uuid = uuid,
        name = name,
        tag = tag,
        createdBy = createdBy,
        description = description,
        discordInvite = discordInvite,
        clanTagColor = clanTagColor,
        _members = emptySet(),
        _invites = emptySet(),
        createdAt = createdAt,
        updatedAt = updatedAt
    )

}