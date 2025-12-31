package dev.slne.clan.velocity.redis.event

import dev.slne.clan.velocity.redis.serializer.SerializableComponent
import dev.slne.surf.redis.event.RedisEvent
import kotlinx.serialization.Serializable

@Serializable
data class ClanBroadcastRedisEvent(
    val clanName: String,
    val message: SerializableComponent
) : RedisEvent()