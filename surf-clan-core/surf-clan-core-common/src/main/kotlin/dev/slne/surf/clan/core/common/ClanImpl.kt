package dev.slne.surf.clan.core.common

import dev.slne.surf.bitmap.bitmaps.Bitmaps
import dev.slne.surf.clan.api.common.Clan
import dev.slne.surf.clan.api.common.invite.ClanInvite
import dev.slne.surf.clan.api.common.member.ClanMember
import dev.slne.surf.clan.api.common.member.ClanMemberRole
import dev.slne.surf.clan.api.common.permission.ClanPermission
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.cloud.api.common.netty.network.codec.kotlinx.java.SerializableUUID
import dev.slne.surf.cloud.api.common.netty.network.codec.kotlinx.java.SerializableZonedDateTime
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ClanImpl(
    override val uuid: SerializableUUID,
    override val name: String,
    override val tag: String,
    override val createdBy: SerializableUUID,
    override val description: String?,
    override var discordInvite: String?,
    override var clanTagColor: Bitmaps?,
    private val _members: Set<ClanMember>,
    private val _invites: Set<ClanInvite>,
    override val createdAt: SerializableZonedDateTime?,
    override val updatedAt: SerializableZonedDateTime?
) : Clan {
    override val members = _members.toObjectSet()
    override val invites = _invites.toObjectSet()
    
    override fun invite(uuid: UUID, invitedBy: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeInvite(uuid: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun isMember(uuid: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun addMember(uuid: UUID, role: ClanMemberRole, addedBy: UUID): Boolean {
        TODO("Not yet implemented")
    }

    override fun addMember(member: ClanMember): Boolean {
        TODO("Not yet implemented")
    }

    override fun removeMember(member: ClanMember): Boolean {
        TODO("Not yet implemented")
    }

    override fun hasPermission(clanMember: ClanMember, permission: ClanPermission): Boolean {
        TODO("Not yet implemented")
    }

    override fun getMember(clanPlayer: ClanPlayer): ClanMember? {
        TODO("Not yet implemented")
    }

    override fun getTranslatedClanTag(): String {
        TODO("Not yet implemented")
    }
}