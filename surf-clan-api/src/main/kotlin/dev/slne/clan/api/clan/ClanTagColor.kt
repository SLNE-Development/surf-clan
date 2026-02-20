package dev.slne.clan.api.clan

import dev.slne.surf.surfapi.core.api.serializer.adventure.component.shadowcolor.SerializableShadowColor
import dev.slne.surf.surfapi.core.api.serializer.adventure.component.textcolor.SerializableTextColor
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor

@Serializable
data class ClanTagColor(
    val foregroundColor: SerializableTextColor = Clan.DEFAULT_CLAN_TAG_FOREGROUND_COLOR,
    val backgroundColor: SerializableTextColor = Clan.DEFAULT_CLAN_TAG_BACKGROUND_COLOR,
    val shadowColor: SerializableShadowColor = Clan.DEFAULT_CLAN_TAG_SHADOW_COLOR
) {

    @ConsistentCopyVisibility
    data class Update private constructor(
        internal val foreground: Field<TextColor>,
        internal val background: Field<TextColor>,
        internal val shadow: Field<ShadowColor>
    ) {
        class Builder @PublishedApi internal constructor() {
            private var foreground: Field<TextColor> = Field.Unset
            private var background: Field<TextColor> = Field.Unset
            private var shadow: Field<ShadowColor> = Field.Unset

            fun foreground(color: TextColor) {
                foreground = Field.Set(color)
            }

            fun background(color: TextColor) {
                background = Field.Set(color)
            }

            fun shadow(color: ShadowColor) {
                shadow = Field.Set(color)
            }

            fun resetForeground() {
                foreground = Field.Reset
            }

            fun resetBackground() {
                background = Field.Reset
            }

            fun resetShadow() {
                shadow = Field.Reset
            }

            @PublishedApi
            internal fun build() = Update(foreground, background, shadow)
        }
    }

    companion object {
        inline fun update(block: Update.Builder.() -> Unit): Update {
            return Update.Builder().apply(block).build()
        }

        fun withDefaultsAsFallback(
            foreground: TextColor?,
            background: TextColor?,
            shadow: ShadowColor?
        ): ClanTagColor {
            return ClanTagColor(
                foregroundColor = foreground ?: Clan.DEFAULT_CLAN_TAG_FOREGROUND_COLOR,
                backgroundColor = background ?: Clan.DEFAULT_CLAN_TAG_BACKGROUND_COLOR,
                shadowColor = shadow ?: Clan.DEFAULT_CLAN_TAG_SHADOW_COLOR
            )
        }

        fun clanTagColorOrNull(
            foreground: TextColor?,
            background: TextColor?,
            shadow: ShadowColor?
        ): ClanTagColor? {
            if (foreground == null || background == null || shadow == null) return null

            return ClanTagColor(
                foregroundColor = foreground,
                backgroundColor = background,
                shadowColor = shadow
            )
        }
    }

    internal sealed interface Field<out T> {
        data object Unset : Field<Nothing>
        data object Reset : Field<Nothing>
        data class Set<T>(val value: T) : Field<T>
    }

    fun applyUpdate(update: Update): ClanTagColor {
        fun <T> Field<T>.resolve(current: T, default: T): T =
            when (this) {
                Field.Unset -> current
                Field.Reset -> default
                is Field.Set -> value
            }

        return copy(
            foregroundColor = update.foreground.resolve(foregroundColor, foregroundColor),
            backgroundColor = update.background.resolve(backgroundColor, backgroundColor),
            shadowColor = update.shadow.resolve(shadowColor, shadowColor),
        )
    }
}