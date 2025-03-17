package dev.slne.clan.core.entities

import dev.slne.clan.api.invite.ClanInvite
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime

object ClanInviteTable : LongIdTable("clan_invites") {

    val invited = reference("invited", ClanPlayerTable.uuid)
    val invitedBy = reference("invited_by", ClanPlayerTable.uuid)
    val clan = reference("clan_id", ClanTable.uuid)

    val createdAt = datetime("created_at").defaultExpression(CurrentDateTime)
    val updatedAt = datetime("updated_at").defaultExpression(CurrentDateTime)
}

class ClanInviteEntity(id: EntityID<Long>) : LongEntity(id), ClanInvite {

    companion object : LongEntityClass<ClanInviteEntity>(ClanInviteTable)

    override var invited by ClanPlayerEntity referencedOn ClanInviteTable.invited
    override var invitedBy by ClanPlayerEntity referencedOn ClanInviteTable.invitedBy
    var clan by ClanEntity referencedOn ClanInviteTable.clan

    override var createdAt by ClanInviteTable.createdAt
    override var updatedAt by ClanInviteTable.updatedAt

}