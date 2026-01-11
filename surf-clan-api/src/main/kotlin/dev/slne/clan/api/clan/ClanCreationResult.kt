package dev.slne.clan.api.clan

sealed interface ClanCreationResult {
    data class InvalidTagOrName(val reason: ClanValidationResult) : ClanCreationResult
    data object OwnerIsAlreadyInClan : ClanCreationResult
    data object ClanAlreadyExists : ClanCreationResult
    data object ClanTagAlreadyExists : ClanCreationResult
    data object ClanNameAlreadyExists : ClanCreationResult
    data class Success(val clan: Clan) : ClanCreationResult
}