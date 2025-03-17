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

    val createdBy: ClanPlayer

    val description: String?
    var discordInvite: String?

    val members: ObjectSet<ClanMember>
    val invites: ObjectSet<ClanInvite>

    val createdAt: LocalDateTime?
    var updatedAt: LocalDateTime?

    suspend fun invite(player: ClanPlayer, invitedBy: ClanPlayer): Boolean
    suspend fun uninvite(player: ClanPlayer): Boolean

    fun isMember(player: ClanPlayer): Boolean
    fun addMember(player: ClanPlayer, role: ClanMemberRole, addedBy: ClanPlayer): Boolean
    fun removeMember(member: ClanMember): Boolean

    fun hasPermission(clanMember: ClanMember, permission: ClanPermission): Boolean
    fun getMember(clanPlayer: ClanPlayer): ClanMember?

}