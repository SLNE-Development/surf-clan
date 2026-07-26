package dev.slne.clan.paper.tagcolor

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.clan.ClanTagColor

/**
 * Which of the three clan tag color permissions the clan owner currently holds.
 */
data class ClanTagColorGrants(
    val foreground: Boolean,
    val background: Boolean,
    val shadow: Boolean
)

/**
 * Which parts of a clan tag color have to fall back to their default.
 */
data class ClanTagColorResets(
    val foreground: Boolean,
    val background: Boolean,
    val shadow: Boolean
) {
    val any get() = foreground || background || shadow

    fun toUpdate() = ClanTagColor.update {
        if (foreground) resetForeground()
        if (background) resetBackground()
        if (shadow) resetShadow()
    }
}

object ClanTagColorPolicy {

    /**
     * A part is reset when it deviates from its default and the owner lacks the matching permission.
     *
     * Parts that already hold their default value never produce a reset, so that a clan without a
     * custom color never triggers an update.
     */
    fun resetsFor(current: ClanTagColor, grants: ClanTagColorGrants) = ClanTagColorResets(
        foreground = !grants.foreground &&
                current.foregroundColor != Clan.DEFAULT_CLAN_TAG_FOREGROUND_COLOR,
        background = !grants.background &&
                current.backgroundColor != Clan.DEFAULT_CLAN_TAG_BACKGROUND_COLOR,
        shadow = !grants.shadow &&
                current.shadowColor != Clan.DEFAULT_CLAN_TAG_SHADOW_COLOR
    )
}
