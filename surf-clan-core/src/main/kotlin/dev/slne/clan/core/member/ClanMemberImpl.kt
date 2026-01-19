package dev.slne.clan.core.member

import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.surf.surfapi.core.api.serializer.java.datetime.datetime.offset.SerializableOffsetDateTime
import dev.slne.surf.surfapi.core.api.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class ClanMemberImpl(
    val id: ULong,
    override val uuid: SerializableStringUUID,
    override var role: ClanMemberRole,
    override val addedBy: SerializableStringUUID?,
    val createdAt: SerializableOffsetDateTime = OffsetDateTime.now(),
    val updatedAt: SerializableOffsetDateTime? = OffsetDateTime.now()
) : ClanMember {
    override fun hasPermission(clanPermission: ClanPermission): Boolean {
        return role.hasPermission(clanPermission)
    }

    override suspend fun changeRole(role: ClanMemberRole): Boolean {
        val changed = CoreClanMemberService.changeRole(this, role)
        if (changed) {
            this.role = role
        }
        return changed
    }
}