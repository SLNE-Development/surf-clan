package dev.slne.clan.api.serializer

import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializable
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import net.kyori.adventure.text.format.TextColor

typealias SerializableTextColor = @Serializable(with = TextColorSerializer::class) TextColor

object TextColorSerializer : KSerializer<TextColor> {
    override val descriptor = PrimitiveSerialDescriptor("TextColor", PrimitiveKind.STRING)

    override fun serialize(
        encoder: Encoder,
        value: TextColor
    ) = encoder.encodeInt(value.value())

    override fun deserialize(decoder: Decoder): TextColor = TextColor.color(decoder.decodeInt())
}