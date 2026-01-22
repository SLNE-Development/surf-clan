package dev.slne.clan.core.clan

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.config.ClanConfig
import dev.slne.clan.core.member.ClanMemberImpl
import dev.slne.surf.bitmap.common.provider.BitmapProvider
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.serializer.adventure.component.textcolor.SerializableTextColor
import dev.slne.surf.surfapi.core.api.serializer.java.datetime.datetime.ldt.SerializableLocalDateTime
import dev.slne.surf.surfapi.core.api.serializer.java.datetime.datetime.offset.SerializableOffsetDateTime
import dev.slne.surf.surfapi.core.api.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import java.util.*

@Serializable
data class ClanImpl(
    val id: ULong,
    override val uuid: SerializableStringUUID,
    override val name: String,
    override val tag: String,
    override val createdByUuid: SerializableStringUUID,
    override var description: String?,
    override var discordInvite: String?,
    override var clanTagColor:  SerializableTextColor,
    override var members: Set<ClanMemberImpl>,
    override val updatedAt: SerializableOffsetDateTime,
    override val createdAt: SerializableOffsetDateTime
) : Clan {

    override suspend fun setDescription(description: String?) {
        if (CoreClanService.updateDescription(this, description)) {
            this.description = description
        }
    }

    override suspend fun setDiscordInvite(discordInvite: String?) {
        if (CoreClanService.updateDiscordInvite(this, discordInvite)) {
            this.discordInvite = discordInvite
        }
    }

    override suspend fun setClanTagColor(color: TextColor) {
        if (CoreClanService.updateTagColor(this, color)) {
            this.clanTagColor = color
        }
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

    override fun isMember(uuid: UUID): Boolean {
        return members.any { member -> member.uuid == uuid }
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

    override fun hasMemberPermission(
        uuid: UUID,
        permission: ClanPermission
    ): Boolean {
        return getMember(uuid)?.hasPermission(permission) ?: false
    }

    override fun getMember(uuid: UUID): ClanMember? {
        return members.find { member -> member.uuid == uuid }
    }

    override fun getRichClanTag(): Component {
        return BitmapProvider.translateToComponent(tag, Colors.WHITE, clanTagColor)
    }

    override suspend fun renderClanTag(minSize: Int): Component {
        if (tag.isBlank()) return Component.empty()
        if (members.size < minSize && tag !in ClanConfig.getConfig().whitelistedTags) return Component.empty()

        return getRichClanTag()
    }

    override suspend fun delete(): Boolean {
        return CoreClanService.delete(this)
    }

}