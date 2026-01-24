package dev.slne.clan.api.clan

/**
 * Represents the result of attempting to create a clan.
 *
 * This sealed interface provides various outcomes when trying to create a new clan,
 * including validation failures and conflicts with existing clans.
 *
 * @see Clan.createClan
 */
sealed interface ClanCreationResult {
    /**
     * Indicates that the clan name or tag failed validation.
     *
     * @property reason the specific validation failure
     */
    data class InvalidTagOrName(val reason: ClanValidationResult) : ClanCreationResult

    /**
     * Indicates that the clan could not be created because the owner is already
     * a member of another clan.
     */
    data object OwnerIsAlreadyInClan : ClanCreationResult

    /**
     * Indicates that a clan with the same name and tag already exists.
     */
    data object ClanAlreadyExists : ClanCreationResult

    /**
     * Indicates that a clan with the same tag already exists.
     */
    data object ClanTagAlreadyExists : ClanCreationResult

    /**
     * Indicates that a clan with the same name already exists.
     */
    data object ClanNameAlreadyExists : ClanCreationResult

    /**
     * Indicates that the clan was successfully created.
     *
     * @property clan the newly created clan
     */
    data class Success(val clan: Clan) : ClanCreationResult
}