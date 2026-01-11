package dev.slne.clan.velocity.redis.event

import dev.slne.clan.core.redis.RedisService
import dev.slne.surf.redis.event.RedisEvent
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import dev.slne.surf.surfapi.core.api.serializer.adventure.component.SerializableComponent
import dev.slne.surf.surfapi.core.api.serializer.java.uuid.SerializableStringUUID
import kotlinx.serialization.Serializable
import java.util.*

@Serializable
class BroadcastMessageEvent(
    val message: SerializableComponent,
    val receiver: Set<SerializableStringUUID>
) : RedisEvent() {
    companion object {
        fun create(receiver: Set<UUID>, message: SurfComponentBuilder.() -> Unit): BroadcastMessageEvent {
            val message = SurfComponentBuilder(message)

            return BroadcastMessageEvent(message, receiver)
        }

        fun broadcast(receiver: Set<UUID>, message: SurfComponentBuilder.() -> Unit) =
            RedisService.publish(create(receiver, message))
    }
}