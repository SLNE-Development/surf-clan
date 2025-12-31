package dev.slne.clan.api.invite

import dev.slne.clan.api.serializer.SerializableLocalDateTime
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ClanInvite(
    val invited: @Contextual UUID,
    val invitedByUuid: @Contextual UUID,

    val createdAt: SerializableLocalDateTime,
    val updatedAt: SerializableLocalDateTime?
)