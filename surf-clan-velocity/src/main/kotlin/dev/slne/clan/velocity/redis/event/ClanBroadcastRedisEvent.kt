package dev.slne.clan.velocity.redis.event

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.core.clan.ClanImpl
import dev.slne.clan.core.redis.RedisService
import dev.slne.surf.redis.event.RedisEvent
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.serializer.adventure.component.SerializableComponent
import kotlinx.serialization.Serializable

@Serializable
data class ClanBroadcastRedisEvent(
    val clanID: ULong,
    val message: SerializableComponent
) : RedisEvent() {
    companion object {
        fun create(clan: Clan, message: SurfComponentBuilder.() -> Unit): ClanBroadcastRedisEvent {
            val clanID = (clan as ClanImpl).id
            val message = SurfComponentBuilder(message)
            return ClanBroadcastRedisEvent(clanID, message)
        }

        fun broadcast(clan: Clan, message: SurfComponentBuilder.() -> Unit) =
            RedisService.publish(create(clan, message))
    }
}