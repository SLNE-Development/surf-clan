package dev.slne.clan.api.clan.listener

import dev.slne.clan.api.clan.Clan

/**
 * Listener for clan update events.
 *
 * This functional interface is invoked whenever a clan's properties are modified,
 * such as changes to the description, Discord invite, or tag color.
 * As a SAM (Single Abstract Method) interface, it can be implemented using lambda expressions.
 *
 * Note: This listener is not triggered by member additions or removals. Use [ClanUpdateMemberListener]
 * for member-related changes.
 *
 * Example usage:
 * ```kotlin
 * val listener = ClanUpdatedListener { clan ->
 *     println("Clan updated: ${clan.name}")
 * }
 * Clan.registerListener(listener)
 * ```
 *
 * @see ClanListener
 * @see ClanUpdateMemberListener
 * @see Clan.registerListener
 * @see Clan.unregisterListener
 */
fun interface ClanUpdatedListener : ClanListener {

    /**
     * Called when a clan's properties have been updated.
     *
     * This method is invoked after the changes have been successfully applied.
     *
     * @param clan the updated clan
     */
    fun onClanUpdated(clan: Clan)
}