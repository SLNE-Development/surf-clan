package dev.slne.clan.core.entities

import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

object ClanMemberTable : LongIdTable("clan_members") {

    val uuid = char("uuid", 36).uniqueIndex().transform({ UUID.fromString(it) }, { it.toString() })
    val addedBy = reference("added_by", ClanPlayerTable.uuid)
    val role = enumerationByName<ClanMemberRole>("role", 255)
    val clan = reference("clan_id", ClanTable.uuid)

    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

}

class ClanMemberEntity(id: EntityID<Long>) : LongEntity(id), ClanMember {

    companion object : LongEntityClass<ClanMemberEntity>(ClanMemberTable)

    override var uuid by ClanMemberTable.uuid
    override var addedBy by ClanPlayerEntity referencedOn ClanMemberTable.addedBy
    override var role by ClanMemberTable.role
    var clan by ClanEntity referencedOn ClanMemberTable.clan

    override var createdAt by ClanMemberTable.createdAt
    override var updatedAt by ClanMemberTable.updatedAt

    fun updateClanMember(block: ClanMemberEntity.() -> Unit) {
        block()
        updatedAt = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
    }

    override fun toString(): String {
        return "ClanMemberEntity(uuid='$uuid', addedBy=$addedBy, role=$role, clan=$clan, createdAt=$createdAt, updatedAt=$updatedAt)"
    }

}