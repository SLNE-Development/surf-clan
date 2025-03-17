package dev.slne.clan.core.entities

import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import it.unimi.dsi.fastutil.objects.ObjectSet
import kotlinx.coroutines.Dispatchers
import org.jetbrains.exposed.dao.LongEntity
import org.jetbrains.exposed.dao.LongEntityClass
import org.jetbrains.exposed.dao.id.EntityID
import org.jetbrains.exposed.dao.id.LongIdTable
import org.jetbrains.exposed.sql.javatime.CurrentDateTime
import org.jetbrains.exposed.sql.javatime.datetime
import org.jetbrains.exposed.sql.transactions.experimental.newSuspendedTransaction
import java.time.ZoneOffset
import java.time.ZonedDateTime
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

class ClanEntity(id: EntityID<Long>) : LongEntity(id), Clan {

    companion object : LongEntityClass<ClanEntity>(ClanTable)

    override var uuid by ClanTable.uuid
    override var name by ClanTable.name
    override var tag by ClanTable.tag

    override var description by ClanTable.description
    override var createdBy by ClanPlayerEntity referencedOn ClanTable.createdBy
    override var discordInvite by ClanTable.discordInvite

    override var createdAt by ClanTable.createdAt
    override var updatedAt by ClanTable.updatedAt

    private val _members by ClanMemberEntity referrersOn ClanMemberTable.clan
    override val members: ObjectSet<ClanMember> = _members.toObjectSet()

    private val _invites by ClanInviteEntity referrersOn ClanInviteTable.clan
    override val invites: ObjectSet<ClanInvite> = _invites.toObjectSet()

    override suspend fun invite(player: ClanPlayer, invitedBy: ClanPlayer): Boolean =
        newSuspendedTransaction(Dispatchers.IO) {
            if (invites.any { it.invited == player }) return false

            val invite = ClanInviteEntity.new {
                this.invited = player
                this.invitedBy = invitedBy
                this.clan = this@ClanEntity
            }

            true
        }

    override suspend fun uninvite(player: ClanPlayer): Boolean {
        TODO("Not yet implemented")
    }

    override fun isMember(player: ClanPlayer): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addMember(
        player: ClanPlayer,
        role: ClanMemberRole,
        addedBy: ClanPlayer
    ): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun addMember(member: ClanMember): Boolean {
        TODO("Not yet implemented")
    }

    override suspend fun removeMember(member: ClanMember): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(clanMember: ClanMember, permission: ClanPermission): Boolean {
        TODO("Not yet implemented")
    }

    override fun getMember(clanPlayer: ClanPlayer): ClanMember? {
        TODO("Not yet implemented")
    }

    fun updateClan(block: ClanEntity.() -> Unit) {
        block()
        updatedAt = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()
    }
}