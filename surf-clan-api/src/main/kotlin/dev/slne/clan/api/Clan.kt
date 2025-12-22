package dev.slne.clan.api

import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.surf.bitmap.bitmaps.Bitmaps
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import it.unimi.dsi.fastutil.objects.ObjectSet
import java.time.LocalDateTime
import java.util.*

data class Clan(
    val uuid: UUID,
    val name: String,
    val tag: String,

    val createdBy: UUID,

    val description: String? = null,
    var discordInvite: String? = null,
    var clanTagColor: Bitmaps? = null,

    val members: ObjectSet<ClanMember> = mutableObjectSetOf(),
    val invites: ObjectSet<ClanInvite> = mutableObjectSetOf(),

    val createdAt: LocalDateTime = LocalDateTime.now(),
    val updatedAt: LocalDateTime? = null,
) {
    fun invite(uuid: UUID, invitedBy: UUID) = invites.add(
        ClanInvite(
            invited = uuid,
            invitedByUuid = invitedBy,
            createdAt = LocalDateTime.now(),
            updatedAt = null
        )
    )

    fun uninvite(uuid: UUID) = invites.removeIf { it.invited == uuid }
    fun isMember(uuid: UUID) = members.any { it.uuid == uuid }
    fun addMember(member: ClanMember) = members.add(member)
    fun addMember(uuid: UUID, role: ClanMemberRole, addedBy: UUID?) = addMember(
        ClanMember(
            uuid = uuid,
            addedBy = addedBy,
            role = role,
        )
    )

    fun removeMember(member: ClanMember) = members.remove(member)
    fun hasPermission(clanMember: ClanMember, permission: ClanPermission) =
        clanMember.role.hasPermission(permission)

    fun getMember(clanPlayer: ClanPlayer): ClanMember? = members.find { it.uuid == clanPlayer.uuid }
    fun getTranslatedClanTag(): String {
        val provider = if (clanTagColor == null) {
            Bitmaps.CLAN_DEFAULT.provider
        } else {
            clanTagColor!!.provider
        }

        return provider.translateToString(tag)
    }

}