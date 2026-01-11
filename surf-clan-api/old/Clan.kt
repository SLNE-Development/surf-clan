package dev.slne.clan.api

import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.clan.api.serializer.SerializableLocalDateTime
import dev.slne.clan.api.serializer.SerializableTextColor
import dev.slne.surf.bitmap.common.provider.BitmapProvider
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class Clan(
    val uuid: @Contextual UUID,
    val name: String,
    val tag: String,

    val createdBy: @Contextual UUID,

    val description: String? = null,
    var discordInvite: String? = null,
    var clanTagColor: SerializableTextColor = Colors.WHITE,

    val members: MutableSet<ClanMember> = mutableObjectSetOf(),
    val invites: MutableSet<ClanInvite> = mutableObjectSetOf(),

    val createdAt: SerializableLocalDateTime = LocalDateTime.now(),
    val updatedAt: SerializableLocalDateTime? = null,
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
    fun getTranslatedClanTag() =
        BitmapProvider.translateToComponent(tag, Colors.WHITE, clanTagColor)

}