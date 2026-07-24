package dev.slne.surf.clan.core.clan

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanTagColor
import dev.slne.clan.api.clan.update.ClanNameAndTag
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.api.core.util.mutableObjectSetOf
import dev.slne.surf.clan.core.member.ClanMemberImpl
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime
import java.util.*

@Serializable
data class ClanImpl(
    override val clanID: ULong,
    override val uuid: @Contextual UUID,
    override var name: String,
    override var tag: String,
    override val createdByUuid: @Contextual UUID,
    override var clanTagColor: ClanTagColor?,
    override var description: String?,
    override var discordInvite: String?,
    override var members: Set<ClanMemberImpl>,
    override val updatedAt: @Contextual OffsetDateTime,
    override val createdAt: @Contextual OffsetDateTime,
) : AbstractClanView(), Clan {
    override suspend fun setDescription(description: String?) {
        CoreClanService.updateDescription(this, description)
    }

    override suspend fun setDiscordInvite(discordInvite: String?) {
        CoreClanService.updateDiscordInvite(this, discordInvite)
    }

    override suspend fun changeClanTagColor(update: ClanTagColor.Update) {
        CoreClanService.updateTagColor(this, update)
    }

    override suspend fun updateClanNameAndTag(update: ClanNameAndTag.Update): ClanNameAndTag.UpdateResult {
        return CoreClanService.updateClanNameAndTag(this, update)
    }

    override suspend fun testClanNameAndTagUpdate(update: ClanNameAndTag.Update): ClanNameAndTag.UpdateResult {
        return CoreClanService.testClanNameAndTagUpdate(this, update)
    }

    override suspend fun getPendingInvites(): Set<ClanInvite> {
        return CoreClanService.fetchPendingInvites(this)
    }

    override suspend fun invite(invitee: UUID, invitedBy: UUID): ClanInviteResult {
        return CoreClanService.invitePlayer(this, invitee, invitedBy)
    }

    override suspend fun revokeInvite(uuid: UUID): Boolean {
        return CoreClanService.revokeInvite(this, uuid)
    }

    override suspend fun addMember(
        uuid: UUID,
        role: ClanMemberRole,
        addedBy: UUID?
    ): ClanMemberAddResult {
        return CoreClanService.addMember(this, uuid, role, addedBy)
    }

    override suspend fun removeMember(member: ClanMember): Boolean {
        return removeMember(member.uuid)
    }

    override suspend fun removeMember(uuid: UUID): Boolean {
        return CoreClanService.removeMember(this, uuid)
    }

    override fun getMember(uuid: UUID): ClanMember? {
        return members.find { member -> member.uuid == uuid }
    }

    override suspend fun delete(): Boolean {
        return CoreClanService.delete(this)
    }

    override fun view() = ClanViewImpl(
        clanID = clanID,
        uuid = uuid,
        name = name,
        tag = tag,
        createdByUuid = createdByUuid,
        description = description,
        discordInvite = discordInvite,
        clanTagColor = clanTagColor,
        members = members.mapTo(mutableObjectSetOf()) { it.view() },
        updatedAt = updatedAt,
        createdAt = createdAt
    )

}