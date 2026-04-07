package dev.slne.surf.clan.core.member

import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.surfapi.core.api.serializer.java.datetime.datetime.offset.SerializableOffsetDateTime
import dev.slne.surf.surfapi.core.api.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable
import java.time.OffsetDateTime

@Serializable
data class ClanMemberImpl(
    val ID: ULong,
    override val uuid: SerializableStringUUID,
    override var role: ClanMemberRole,
    override val addedBy: SerializableStringUUID?,
    val createdAt: SerializableOffsetDateTime = OffsetDateTime.now(),
    val updatedAt: SerializableOffsetDateTime? = OffsetDateTime.now()
) : AbstractClanMemberView(), ClanMember {
    override suspend fun changeRole(role: ClanMemberRole): Boolean {
        return CoreClanMemberService.Companion.changeRole(this, role)
    }

    override fun view() = ClanMemberViewImpl(
        uuid = uuid,
        role = role,
        addedBy = addedBy
    )
}