package dev.slne.clan.core.entities

import dev.slne.clan.api.player.ClanPlayer
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import java.util.*

object ClanPlayerTable : LongIdTable("clan_players") {

    val username = varchar("username", 16)
    val uuid = char("uuid", 36).uniqueIndex().transform({ UUID.fromString(it) }, { it.toString() })
    val acceptsClanInvites = bool("accepts_clan_invites")

}

class ClanPlayerEntity(id: EntityID<Long>) : LongEntity(id), ClanPlayer {

    companion object : LongEntityClass<ClanPlayerEntity>(ClanPlayerTable)

    override var username by ClanPlayerTable.username
    override var uuid by ClanPlayerTable.uuid
    override var acceptsClanInvites by ClanPlayerTable.acceptsClanInvites

    override fun toString(): String {
        return "ClanPlayerEntity(username='$username', uuid=$uuid, acceptsClanInvites=$acceptsClanInvites)"
    }

}