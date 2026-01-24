package dev.slne.clan.api.clan.listener

/**
 * Marker interface for all clan-related event listeners.
 *
 * This sealed interface serves as the base type for all listener interfaces that handle
 * clan events. Implement one of its subtypes to receive specific event notifications.
 *
 * @see ClanCreatedListener
 * @see ClanDeletedListener
 * @see ClanUpdatedListener
 * @see ClanUpdateMemberListener
 */
sealed interface ClanListener