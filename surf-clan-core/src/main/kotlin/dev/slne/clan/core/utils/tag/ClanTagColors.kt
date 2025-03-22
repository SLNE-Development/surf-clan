package dev.slne.clan.core.utils.tag

import dev.slne.clan.api.Clan
import dev.slne.clan.api.tag.ClanTagColor
import dev.slne.clan.core.utils.tag.colors.*
import dev.slne.surf.surfapi.core.api.util.freeze
import dev.slne.surf.surfapi.core.api.util.objectSetOf

object ClanTagColors {

    private val _colors = objectSetOf(
        BlueClanTagColorMap(),
        GreenClanTagColorMap(),
        OrangeClanTagColorMap(),
        PurpleClanTagColorMap(),
        RedClanTagColorMap(),
        WhiteClanTagColorMap(),
        YellowClanTagColorMap()
    )
    val colors = _colors.freeze()
    val DEFAULT = _colors.first { it.clanTagColor == ClanTagColor.WHITE }

    fun translateClanTag(clan: Clan): String {
        val color = clan.clanTagColor ?: DEFAULT.clanTagColor
        val map = _colors.firstOrNull { it.clanTagColor == color } ?: DEFAULT

        return map.translateClanTag(clan.tag)
    }
}