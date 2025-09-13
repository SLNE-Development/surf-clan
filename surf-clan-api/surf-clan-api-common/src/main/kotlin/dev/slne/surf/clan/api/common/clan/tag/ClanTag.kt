package dev.slne.surf.clan.api.common.clan.tag

import dev.slne.surf.bitmap.common.provider.BitmapProvider
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import net.kyori.adventure.text.ComponentLike
import net.kyori.adventure.text.format.TextColor

@Serializable
data class ClanTag(
    val tag: String,
    val foregroundColor: @Contextual TextColor,
    val shadowColor: @Contextual TextColor?,
    val backgroundColor: @Contextual TextColor
) : ComponentLike {
    override fun asComponent() = BitmapProvider.translateToComponent(
        input = tag,
        foregroundColor = foregroundColor,
        shadowColor = shadowColor,
        backgroundColor = backgroundColor,
    )
}