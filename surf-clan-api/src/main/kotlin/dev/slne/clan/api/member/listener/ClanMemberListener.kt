package dev.slne.clan.api.member.listener

/**
 * Marker interface for all clan member-related event listeners.
 *
 * This sealed interface serves as the base type for all listener interfaces that handle
 * clan member events. Implement one of its subtypes to receive specific event notifications.
 *
 * @see ClanMemberChangedRoleListener
 */
sealed interface ClanMemberListener