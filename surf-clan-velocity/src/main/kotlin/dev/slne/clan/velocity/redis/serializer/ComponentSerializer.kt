package dev.slne.clan.velocity.redis.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer

typealias SerializableComponent = @Serializable(with = ComponentSerializer::class) Component

object ComponentSerializer : KSerializer<Component> {
    override val descriptor = PrimitiveSerialDescriptor("Component", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: Component
    ) = encoder.encodeString(GsonComponentSerializer.gson().serialize(value))

    override fun deserialize(decoder: Decoder) =
        GsonComponentSerializer.gson().deserialize(decoder.decodeString())
}