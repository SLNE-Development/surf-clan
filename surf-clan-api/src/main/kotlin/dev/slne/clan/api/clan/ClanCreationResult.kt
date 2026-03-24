package dev.slne.clan.api.clan

import kotlinx.serialization.Serializable

/**
 * Represents the result of attempting to create a clan.
 *
 * This sealed interface provides various outcomes when trying to create a new clan,
 * including validation failures and conflicts with existing clans.
 *
 * @see Clan.createClan
 */
@Serializable
sealed interface ClanCreationResult {
    /**
     * Indicates that the clan name or tag failed validation.
     *
     * @property reason the specific validation failure
     */
    @Serializable
    data class InvalidTagOrName(val reason: ClanValidationResult) : ClanCreationResult

    /**
     * Indicates that the clan could not be created because the owner is already
     * a member of another clan.
     */
    @Serializable
    data object OwnerIsAlreadyInClan : ClanCreationResult

    /**
     * Indicates that a clan with the same name and tag already exists.
     */
    @Serializable
    data object ClanAlreadyExists : ClanCreationResult

    /**
     * Indicates that a clan with the same tag already exists.
     */
    @Serializable
    data object ClanTagAlreadyExists : ClanCreationResult

    /**
     * Indicates that a clan with the same name already exists.
     */
    @Serializable
    data object ClanNameAlreadyExists : ClanCreationResult

    /**
     * Indicates that the clan was successfully created.
     *
     * @property clan the newly created clan
     */
    @Serializable
    data class Success(val clan: Clan) : ClanCreationResult
}