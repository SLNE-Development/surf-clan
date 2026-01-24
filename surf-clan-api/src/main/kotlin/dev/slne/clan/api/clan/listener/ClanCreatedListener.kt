package dev.slne.clan.api.clan.listener

import dev.slne.clan.api.clan.Clan

/**
 * Listener for clan creation events.
 *
 * This functional interface is invoked whenever a new clan is created.
 * As a SAM (Single Abstract Method) interface, it can be implemented using lambda expressions.
 *
 * Example usage:
 * ```kotlin
 * val listener = ClanCreatedListener { clan ->
 *     println("New clan created: ${clan.name}")
 * }
 * Clan.registerListener(listener)
 * ```
 *
 * @see ClanListener
 * @see Clan.registerListener
 * @see Clan.unregisterListener
 */
fun interface ClanCreatedListener : ClanListener {
    /**
     * Called when a new clan has been created.
     *
     * This method is invoked after the clan has been successfully persisted.
     *
     * @param clan the newly created clan
     */
    fun onClanCreated(clan: Clan)
}