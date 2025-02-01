package dev.slne.clan.api

import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.player.ClanPlayer
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.time.LocalDateTime
import java.util.*

interface Clan {

    val uuid: UUID
    val name: String
    val tag: String

    val createdBy: UUID

    val description: String?
    val discordInvite: String?

    val members: ObjectSet<ClanMember>
    val invites: ObjectSet<ClanInvite>

    val createdAt: LocalDateTime?
    val updatedAt: LocalDateTime?

    fun invite(uuid: UUID, invitedBy: UUID): Boolean
    fun uninvite(uuid: UUID): Boolean

    fun isMember(uuid: UUID): Boolean
    fun addMember(uuid: UUID, role: ClanMemberRole, addedBy: UUID): Boolean
    fun addMember(member: ClanMember): Boolean
    fun removeMember(member: ClanMember): Boolean

    fun hasPermission(clanMember: ClanMember, permission: ClanPermission): Boolean
    fun getMember(clanPlayer: ClanPlayer): ClanMember?

}