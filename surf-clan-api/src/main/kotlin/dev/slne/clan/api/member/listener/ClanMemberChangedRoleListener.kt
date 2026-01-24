package dev.slne.clan.api.member.listener

import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole

/**
 * Listener for clan member role change events.
 *
 * This functional interface is invoked whenever a clan member's role is changed.
 * As a SAM (Single Abstract Method) interface, it can be implemented using lambda expressions.
 *
 * Example usage:
 * ```kotlin
 * val listener = ClanMemberChangedRoleListener { member, newRole ->
 *     println("${member.uuid} changed to $newRole")
 * }
 * ClanMember.registerListener(listener)
 * ```
 *
 * @see ClanMemberListener
 * @see ClanMember.registerListener
 * @see ClanMember.unregisterListener
 */
fun interface ClanMemberChangedRoleListener : ClanMemberListener {

    /**
     * Called when a clan member's role has been changed.
     *
     * This method is invoked after the role change has been successfully applied.
     *
     * @param member the member whose role was changed
     * @param newRole the newly assigned role
     */
    fun onClanMemberChangedRole(member: ClanMember, newRole: ClanMemberRole)
}