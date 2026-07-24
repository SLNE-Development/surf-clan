package dev.slne.clan.api.clan

import dev.slne.clan.api.clan.listener.ClanListener
import dev.slne.clan.api.clan.update.ClanNameAndTag
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.invite.ClanInviteResult
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import org.jetbrains.annotations.ApiStatus
import java.util.*
import kotlin.contracts.ExperimentalContracts
import kotlin.contracts.InvocationKind
import kotlin.contracts.contract

/**
 * Represents a mutable clan with the ability to perform administrative actions.
 *
 * This interface extends [ClanView] and provides methods for managing clan members,
 * invitations, and settings.
 *
 * @see ClanView
 */
@ApiStatus.NonExtendable
interface Clan : ClanView {
    /**
     * A mutable set of all members in this clan.
     */
    override val members: Set<ClanMember>

    /**
     * Updates the clan's description.
     *
     * @param description the new description, or `null` to remove it
     */
    suspend fun setDescription(description: String?)

    /**
     * Updates the clan's Discord invite link.
     *
     * @param discordInvite the new Discord invite URL, or `null` to remove it
     */
    suspend fun setDiscordInvite(discordInvite: String?)

    suspend fun changeClanTagColor(update: ClanTagColor.Update)

    /**
     * Updates the clan's name and tag.
     */
    suspend fun updateClanNameAndTag(update: ClanNameAndTag.Update): ClanNameAndTag.UpdateResult

    /**
     * Tests whether a proposed clan name and tag update would be valid without actually applying the changes.
     */
    suspend fun testClanNameAndTagUpdate(update: ClanNameAndTag.Update): ClanNameAndTag.UpdateResult

    /**
     * Retrieves all pending invitations sent by this clan.
     *
     * @return a set of mutable pending invitations, or an empty set if none exist
     */
    override suspend fun getPendingInvites(): Set<ClanInvite>

    /**
     * Sends a clan invitation to a player.
     *
     * @param invitee the UUID of the player to invite
     * @param invitedBy the UUID of the member sending the invitation
     * @return a [ClanInviteResult] indicating the outcome of the invitation attempt
     */
    suspend fun invite(invitee: UUID, invitedBy: UUID): ClanInviteResult

    /**
     * Revokes a pending invitation.
     *
     * @param uuid the UUID of the invited player
     * @return `true` if the invitation was successfully revoked, `false` otherwise
     */
    suspend fun revokeInvite(uuid: UUID): Boolean

    /**
     * Adds a new member to the clan.
     *
     * @param uuid the UUID of the player to add
     * @param role the initial role to assign to the new member
     * @param addedBy the UUID of the member who added this player, or `null` if added by the system
     * @return a [ClanMemberAddResult] indicating the outcome of the add operation
     */
    suspend fun addMember(uuid: UUID, role: ClanMemberRole, addedBy: UUID?): ClanMemberAddResult

    /**
     * Removes a member from the clan.
     *
     * @param member the member to remove
     * @return `true` if the member was successfully removed, `false` otherwise
     */
    suspend fun removeMember(member: ClanMember): Boolean

    /**
     * Removes a member from the clan by their UUID.
     *
     * @param uuid the UUID of the member to remove
     * @return `true` if the member was successfully removed, `false` otherwise
     */
    suspend fun removeMember(uuid: UUID): Boolean

    /**
     * Retrieves a specific member of this clan.
     *
     * @param uuid the UUID of the member
     * @return the [ClanMember] if found, or `null` if the player is not a member
     */
    override fun getMember(uuid: UUID): ClanMember?

    /**
     * Permanently deletes this clan and all associated data.
     *
     * This operation cannot be undone.
     *
     * @return `true` if the clan was successfully deleted, `false` otherwise
     */
    suspend fun delete(): Boolean

    /**
     * Returns a read-only view of this clan.
     *
     * Use this method when you need to pass clan information to code that should not
     * be able to modify the clan's data.
     *
     * @return an immutable view of this clan
     */
    fun view(): ClanView

    companion object {
        /**
         * The default color for clan tags when no custom color is set.
         */
        val DEFAULT_CLAN_TAG_BACKGROUND_COLOR: TextColor = TextColor.fromHexString("#39434f")
            ?: error("Failed to parse default clan tag background color")
        val DEFAULT_CLAN_TAG_FOREGROUND_COLOR: TextColor = TextColor.fromHexString("#f0f4f7")
            ?: error("Failed to parse default clan tag foreground color")
        val DEFAULT_CLAN_TAG_SHADOW_COLOR: ShadowColor = ShadowColor.none()

        val DEFAULT_CLAN_TAG_COLORS = ClanTagColor(
            DEFAULT_CLAN_TAG_FOREGROUND_COLOR,
            DEFAULT_CLAN_TAG_BACKGROUND_COLOR,
            DEFAULT_CLAN_TAG_SHADOW_COLOR
        )

        /**
         * The minimum number of members required before a Discord link can be set.
         */
        const val DISCORD_LINK_REQUIRED_MEMBERS = 30

        /**
         * The minimum allowed length for a clan name.
         */
        const val MIN_NAME_LENGTH = 3

        /**
         * The maximum allowed length for a clan name.
         */
        const val MAX_NAME_LENGTH = 16

        /**
         * The minimum allowed length for a clan tag.
         */
        const val MIN_TAG_LENGTH = 3

        /**
         * The maximum allowed length for a clan tag.
         */
        const val MAX_TAG_LENGTH = 4

        /**
         * The cost for renaming a clan.
         */
        const val CLAN_RENAME_COST = 5_000.0

        /**
         * Registers a listener to receive clan-related events.
         *
         * @param listener the listener to register
         * @see ClanListener
         */
        fun registerListener(listener: ClanListener) =
            ClanService.registerListener(listener)

        /**
         * Unregisters a previously registered clan listener.
         *
         * @param listener the listener to unregister
         * @see ClanListener
         */
        fun unregisterListener(listener: ClanListener) =
            ClanService.unregisterListener(listener)

        /**
         * Retrieves the clan that a player is a member of.
         *
         * @param uuid the UUID of the player
         * @return the [Clan] the player belongs to, or `null` if they are not in any clan
         */
        suspend fun byPlayer(uuid: UUID): Clan? = ClanService.findClanByPlayer(uuid)

        /**
         * Retrieves a clan by its unique identifier.
         *
         * @param uuid the UUID of the clan
         * @return the [Clan] if found, or `null` otherwise
         */
        suspend fun byUuid(uuid: UUID): Clan? = ClanService.findClanByUuid(uuid)

        /**
         * Retrieves a clan by its tag.
         *
         * @param tag the clan tag to search for
         * @return the [Clan] if found, or `null` otherwise
         */
        suspend fun byTag(tag: String): Clan? = ClanService.findClanByTag(tag)

        /**
         * Retrieves a clan by its name.
         *
         * @param name the clan name to search for
         * @return the [Clan] if found, or `null` otherwise
         */
        suspend fun byName(name: String): Clan? = ClanService.findClanByName(name)

        /**
         * Validates a clan name and tag against system rules.
         *
         * This should be called before attempting to create a clan to ensure the
         * name and tag meet requirements.
         *
         * @param name the proposed clan name
         * @param tag the proposed clan tag
         * @return a [ClanValidationResult] indicating if the inputs are valid
         */
        fun validateClanNameAndTag(name: String, tag: String): ClanValidationResult =
            ClanService.validateClanNameAndTag(name, tag)

        /**
         * Creates a new clan with the specified parameters.
         *
         * This function uses a builder pattern to configure optional clan properties.
         *
         * Example usage:
         * ```kotlin
         * val result = Clan.createClan("Warriors", "WAR", ownerUuid) {
         *     tagColor(TextColor.color(255, 0, 0))
         *     description("A clan for warriors")
         *     discordInvite("https://discord.gg/example")
         * }
         * ```
         *
         * @param name the name of the new clan
         * @param tag the tag of the new clan
         * @param owner the UUID of the player who will own the clan
         * @param additional optional builder block to configure additional properties
         * @return a [ClanCreationResult] indicating the outcome of the creation attempt
         * @see ClanCreateBuilder
         */
        @OptIn(ExperimentalContracts::class)
        suspend fun createClan(
            name: String,
            tag: String,
            owner: UUID,
            additional: ClanCreateBuilder.() -> Unit = {}
        ): ClanCreationResult {
            contract {
                callsInPlace(additional, InvocationKind.EXACTLY_ONCE)
            }

            val builder = ClanCreateBuilder(name, tag, owner)
            builder.additional()

            return ClanService.createClan(builder.copy())
        }
    }
}

suspend fun UUID.findClanByUuid() = Clan.byUuid(this)
suspend fun UUID.findClanByPlayer() = Clan.byPlayer(this)

suspend inline fun Clan.changeClanTagColor(
    update: ClanTagColor.Update.Builder.() -> Unit
) = changeClanTagColor(
    ClanTagColor.update(update)
)

/**
 * Updates the clan's name and tag.
 */
suspend inline fun Clan.updateClanNameAndTag(
    update: ClanNameAndTag.Update.Builder.() -> Unit
) = updateClanNameAndTag(ClanNameAndTag.update(update))

/**
 * @see Clan.testClanNameAndTagUpdate
 */
suspend inline fun Clan.testClanNameAndTagUpdate(
    update: ClanNameAndTag.Update.Builder.() -> Unit
) = testClanNameAndTagUpdate(ClanNameAndTag.update(update))