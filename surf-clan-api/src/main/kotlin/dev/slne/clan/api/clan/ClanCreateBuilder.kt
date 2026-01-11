package dev.slne.clan.api.clan

import net.kyori.adventure.text.format.TextColor
import java.util.*

@ConsistentCopyVisibility
data class ClanCreateBuilder internal constructor(
    val name: String,
    val tag: String,
    val owner: UUID
) {
    var tagColor: TextColor? = null
        private set
    var description: String? = null
        private set
    var discordInvite: String? = null
        private set

    fun tagColor(tagColor: TextColor) = apply { this.tagColor = tagColor }
    fun description(description: String) = apply { this.description = description }
    fun discordInvite(discordInvite: String) = apply { this.discordInvite = discordInvite }
}