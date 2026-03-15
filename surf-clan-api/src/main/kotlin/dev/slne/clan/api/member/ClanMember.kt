package dev.slne.clan.api.member

import dev.slne.clan.api.member.listener.ClanMemberListener
import org.jetbrains.annotations.ApiStatus
import java.util.*

/**
 * Represents a mutable clan member with the ability to perform actions.
 *
 * This interface extends [ClanMemberView] and provides methods for modifying member data
 * and managing member-related listeners.
 *
 * @see ClanMemberView
 */
@ApiStatus.NonExtendable
interface ClanMember : ClanMemberView {
    /**
     * Changes the role of this clan member.
     *
     * This operation is performed asynchronously and may involve database updates and
     * permission checks.
     *
     * @param role the new role to assign to this member
     * @return `true` if the role change was successful, `false` otherwise
     * @see ClanMemberRole
     */
    suspend fun changeRole(role: ClanMemberRole): Boolean

    /**
     * Returns a read-only view of this clan member.
     *
     * Use this method when you need to pass member information to code that should not
     * be able to modify the member's data.
     *
     * @return an immutable view of this member
     */
    fun view(): ClanMemberView

    companion object {
        /**
         * Registers a listener to receive clan member-related events.
         *
         * @param listener the listener to register
         * @see ClanMemberListener
         */
        fun registerListener(listener: ClanMemberListener) =
            ClanMemberService.registerListener(listener)

        /**
         * Unregisters a previously registered clan member listener.
         *
         * @param listener the listener to unregister
         * @see ClanMemberListener
         */
        fun unregisterListener(listener: ClanMemberListener) =
            ClanMemberService.unregisterListener(listener)

        /**
         * Retrieves a clan member by their unique identifier.
         *
         * @param uuid the unique identifier of the member
         * @return the [ClanMember] instance, or `null` if no member exists with the given UUID
         */
        suspend fun byUuid(uuid: UUID) = ClanMemberService.findMemberByUuid(uuid)

        /**
         * Retrieves a clan member by their player name.
         *
         * @param name the name of the member
         * @return the [ClanMember] instance, or `null` if no member exists with the given name
         */
        suspend fun byName(name: String) = ClanMemberService.findMemberByName(name)
    }
}