package dev.slne.clan.api.clan.listener

import dev.slne.clan.api.clan.ClanView

/**
 * Listener for clan deletion events.
 *
 * This functional interface is invoked whenever a clan is deleted.
 * As a SAM (Single Abstract Method) interface, it can be implemented using lambda expressions.
 *
 * Note that the clan provided is a read-only view since the clan is being deleted and
 * modifications are no longer possible.
 *
 * Example usage:
 * ```kotlin
 * val listener = ClanDeletedListener { clan ->
 *     println("Clan deleted: ${clan.name}")
 * }
 * Clan.registerListener(listener)
 * ```
 *
 * @see ClanListener
 * @see dev.slne.clan.api.clan.Clan.registerListener
 * @see dev.slne.clan.api.clan.Clan.unregisterListener
 */
fun interface ClanDeletedListener : ClanListener {

    /**
     * Called when a clan has been deleted.
     *
     * This method is invoked after the clan has been successfully removed from the system.
     *
     * @param clan a read-only view of the deleted clan
     */
    fun onClanDeleted(clan: ClanView)
}