package dev.slne.clan.core.entities

import dev.slne.clan.core.CoreClan
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import java.util.*

object ClanTable : LongIdTable("clans") {

    val uuid = char("uuid", 36).uniqueIndex().transform({ UUID.fromString(it) }, { it.toString() })
    val name = varchar("name", 255).uniqueIndex()
    val tag = varchar("tag", 4).uniqueIndex()

    val description = largeText("description").nullable()
    val createdBy = reference("created_by", ClanPlayerTable.uuid)
    val discordInvite = varchar("discord_invite", 255).nullable()

    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)

}

class ClanEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<ClanEntity>(ClanTable)

    var uuid by ClanTable.uuid
    var name by ClanTable.name
    var tag by ClanTable.tag

    var description by ClanTable.description
    var createdBy by ClanPlayerEntity referencedOn ClanTable.createdBy
    var discordInvite by ClanTable.discordInvite

    var createdAt by ClanTable.createdAt
    var updatedAt by ClanTable.updatedAt

    val members by ClanMemberEntity referrersOn ClanMemberTable.clan
    val invites by ClanInviteEntity referrersOn ClanInviteTable.clan

    fun toClan() = CoreClan(
        uuid = uuid,
        name = name,
        tag = tag,

        description = description,
        createdBy = createdBy.toClanPlayer(),
        discordInvite = discordInvite,

        createdAt = createdAt,
        updatedAt = updatedAt,
    )

    override fun toString(): String {
        return "ClanEntity(uuid=$uuid, name='$name', tag='$tag', description=$description, createdBy=$createdBy, discordInvite=$discordInvite, createdAt=$createdAt, updatedAt=$updatedAt, members=$members, invites=$invites)"
    }

}