package dev.slne.clan.core.entities

import dev.slne.clan.core.player.CoreClanPlayer
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import java.util.*

object ClanPlayerTable : LongIdTable("clan_players") {

    val username = varchar("username", 16).nullable()
    val uuid = char("uuid", 36).uniqueIndex().transform({ UUID.fromString(it) }, { it.toString() })
    val acceptsClanInvites = bool("accepts_clan_invites")

}

class ClanPlayerEntity(id: EntityID<Long>) : LongEntity(id) {

    companion object : LongEntityClass<ClanPlayerEntity>(ClanPlayerTable)

    var username by ClanPlayerTable.username
    var uuid by ClanPlayerTable.uuid
    var acceptsClanInvites by ClanPlayerTable.acceptsClanInvites

    fun toClanPlayer() = CoreClanPlayer(
        uuid = uuid,
        username = username,
        acceptsClanInvites = acceptsClanInvites
    )

    override fun toString(): String {
        return "ClanPlayerEntity(username='$username', uuid=$uuid, acceptsClanInvites=$acceptsClanInvites)"
    }

}