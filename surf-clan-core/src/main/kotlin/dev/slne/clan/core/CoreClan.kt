package dev.slne.clan.core

import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.player.ClanPlayer
import dev.slne.clan.core.invite.CoreClanInvite
import dev.slne.clan.core.member.CoreClanMember
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.mutableObjectSetOf
import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.util.*

class CoreClan(
    override val uuid: UUID,
    override val name: String,
    override val tag: String,

    override val description: String? = null,
    override var discordInvite: String? = null,
    override val createdBy: ClanPlayer,

    override val createdAt: LocalDateTime? = LocalDateTime.now(),
    override var updatedAt: LocalDateTime? = LocalDateTime.now(),
) : Clan {

    private val _members = mutableObjectSetOf<ClanMember>()
    override val members get() = _members.freeze()

    private val _invites = mutableObjectSetOf<ClanInvite>()
    override val invites get() = _invites.freeze()

    override suspend fun invite(player: ClanPlayer, invitedBy: ClanPlayer): Boolean = updateClan {
        _invites.add(CoreClanInvite(invited = player, invitedBy = invitedBy, clan = this))
    }

    override suspend fun uninvite(player: ClanPlayer): Boolean = updateClan {
        _invites.removeIf { it.invited == player }
    }

    override fun isMember(player: ClanPlayer) = members.any { it.player == player }

    override fun addMember(
        player: ClanPlayer,
        role: ClanMemberRole,
        addedBy: ClanPlayer
    ): Boolean = updateClan {
        _invites.removeIf { it.invited == player }
        _members.add(CoreClanMember(player, role, addedBy))
    }

    override fun removeMember(member: ClanMember): Boolean = updateClan {
        _members.remove(member)
    }

    override fun hasPermission(clanMember: ClanMember, permission: ClanPermission) =
        clanMember.role.hasPermission(permission)

    override fun getMember(clanPlayer: ClanPlayer) = members.find { it.player == clanPlayer }

    private fun <B> updateClan(block: Clan.() -> B): B {
        val result = block()
        updatedAt = ZonedDateTime.now().withZoneSameInstant(ZoneOffset.UTC).toLocalDateTime()

        return result
    }

    override fun toString(): String {
        return "CoreClan(uuid=$uuid, name='$name', tag='$tag', description=$description, discordInvite=$discordInvite, createdBy=$createdBy, createdAt=$createdAt, updatedAt=$updatedAt, _members=$_members, _invites=$_invites)"
    }

}