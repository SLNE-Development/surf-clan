package dev.slne.clan.velocity.redis.event

import dev.slne.redis.event.RedisEvent
import kotlinx.serialization.Serializable

@Serializable
object ClanRefreshRedisEvent : RedisEvent()