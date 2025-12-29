package dev.slne.clan.velocity.redis.event

import dev.slne.surf.redis.event.RedisEvent
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
data class ClanInviteRedisEvent(
    val inviterName: String,
    val invitedUuid: @Contextual UUID,
    val clanName: String
) : RedisEvent()