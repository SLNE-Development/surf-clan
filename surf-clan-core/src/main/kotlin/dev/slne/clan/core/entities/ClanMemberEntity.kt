package dev.slne.clan.core.entities

import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.core.member.CoreClanMember
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object ClanMemberTable : LongIdTable("clan_members") {

    val member = reference("uuid", ClanPlayerTable.uuid).uniqueIndex()
    val addedBy = reference("added_by", ClanPlayerTable.uuid)
    val role = enumerationByName<ClanMemberRole>("role", 255)
    val clan = reference("clan_id", ClanTable.uuid)

    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

}

class ClanMemberEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<ClanMemberEntity>(ClanMemberTable)

    var player by ClanPlayerEntity referencedOn ClanMemberTable.member
    var addedBy by ClanPlayerEntity referencedOn ClanMemberTable.addedBy
    var role by ClanMemberTable.role
    var clan by ClanEntity referencedOn ClanMemberTable.clan

    var createdAt by ClanMemberTable.createdAt
    var updatedAt by ClanMemberTable.updatedAt

    fun toClanMember() = CoreClanMember(
        player = player.toClanPlayer(),
        addedBy = addedBy.toClanPlayer(),
        role = role,
        createdAt = createdAt,
        updatedAt = updatedAt
    )

    override fun toString(): String {
        return "ClanMemberEntity(player=$player, addedBy=$addedBy, role=$role, clan=$clan, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}