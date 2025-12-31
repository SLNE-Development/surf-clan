package dev.slne.clan.api.member

import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.api.serializer.SerializableLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.LocalDateTime
import java.util.*

@Serializable
data class ClanMember(
    val uuid: @Contextual UUID,
    var role: ClanMemberRole,

    val addedBy: @Contextual UUID?,

    val createdAt: SerializableLocalDateTime = LocalDateTime.now(),
    val updatedAt: SerializableLocalDateTime? = LocalDateTime.now()
) {
    fun hasPermission(clanPermission: ClanPermission) = role.hasPermission(clanPermission)
}