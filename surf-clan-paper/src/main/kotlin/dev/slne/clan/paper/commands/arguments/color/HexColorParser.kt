package dev.slne.clan.paper.commands.arguments.color

object HexColorParser {
    private val HEX_REGEX = Regex("^[0-9a-fA-F]{6}([0-9a-fA-F]{2})?$")

    fun normalizeToRgba(input: String): String? {
        val raw = input.trim().removePrefix("#")

        if (!HEX_REGEX.matches(raw)) return null

        val withAlpha = if (raw.length == 6) raw + "FF" else raw
        return "#$withAlpha"
    }
}