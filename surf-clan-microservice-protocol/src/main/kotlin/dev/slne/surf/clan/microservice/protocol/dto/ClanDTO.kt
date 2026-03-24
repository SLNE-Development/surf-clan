package dev.slne.surf.clan.microservice.protocol.dto

import dev.slne.surf.surfapi.core.api.serializer.adventure.component.shadowcolor.SerializableShadowColor
import dev.slne.surf.surfapi.core.api.serializer.adventure.component.textcolor.SerializableTextColor
import dev.slne.surf.surfapi.core.api.serializer.java.datetime.datetime.offset.SerializableOffsetDateTime
import dev.slne.surf.surfapi.core.api.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable

@Serializable
data class ClanDTO(
    val id: ULong,
    val uuid: SerializableStringUUID,
    val name: String,
    val tag: String,
    val createdByUuid: SerializableStringUUID,
    val foregroundTagColor: SerializableTextColor,
    val backgroundTagColor: SerializableTextColor,
    val shadowTagColor: SerializableShadowColor,
    val description: String?,
    val discordInvite: String?,
    val members: Set<ClanMemberImpl>,
    val updatedAt: SerializableOffsetDateTime,
    val createdAt: SerializableOffsetDateTime,
)