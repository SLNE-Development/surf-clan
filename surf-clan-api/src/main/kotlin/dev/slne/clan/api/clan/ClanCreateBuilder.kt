package dev.slne.clan.api.clan

import net.kyori.adventure.text.format.TextColor
import java.util.*

/**
 * Builder class for configuring optional properties when creating a clan.
 *
 * This class uses a fluent API to set optional clan properties such as tag color,
 * description, and Discord invite. Instances are created internally by [Clan.createClan].
 *
 * @property name the name of the clan being created
 * @property tag the tag of the clan being created
 * @property owner the UUID of the clan owner
 * @see Clan.createClan
 */
@ConsistentCopyVisibility
data class ClanCreateBuilder internal constructor(
    val name: String,
    val tag: String,
    val owner: UUID
) {
    /**
     * The color for the clan tag, or `null` to use the default color.
     */
    var tagColor: ClanTagColor? = null
        private set

    /**
     * The optional description for the clan.
     */
    var description: String? = null
        private set

    /**
     * The optional Discord invite link for the clan.
     */
    var discordInvite: String? = null
        private set

    /**
     * Sets the color for the clan tag.
     *
     * @param tagColor the color to use for the clan tag
     * @return this builder for method chaining
     */
    fun tagColor(tagColor: ClanTagColor) = apply { this.tagColor = tagColor }

    /**
     * Sets the description for the clan.
     *
     * @param description the clan description
     * @return this builder for method chaining
     */
    fun description(description: String) = apply { this.description = description }

    /**
     * Sets the Discord invite link for the clan.
     *
     * @param discordInvite the Discord invite URL
     * @return this builder for method chaining
     */
    fun discordInvite(discordInvite: String) = apply { this.discordInvite = discordInvite }
}