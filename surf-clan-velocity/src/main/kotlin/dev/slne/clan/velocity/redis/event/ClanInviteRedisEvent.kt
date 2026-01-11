package dev.slne.clan.velocity.redis.event

import dev.slne.surf.redis.event.RedisEvent
import dev.slne.surf.surfapi.core.api.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable

@Serializable
data class ClanInviteRedisEvent(
    val inviterName: String,
    val invitedUuid: SerializableStringUUID,
    val clanName: String
) : RedisEvent()