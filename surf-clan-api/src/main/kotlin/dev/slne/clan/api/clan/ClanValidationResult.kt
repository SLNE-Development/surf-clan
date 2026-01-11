package dev.slne.clan.api.clan

import it.unimi.dsi.fastutil.chars.Char2BooleanMap

sealed interface ClanValidationResult {
    data object Valid : ClanValidationResult
    data class NameOutOfRange(val min: Int, val max: Int) : ClanValidationResult
    data class TagOutOfRange(val min: Int, val max: Int) : ClanValidationResult
    data class InvalidTagCharacters(val characters: Char2BooleanMap) : ClanValidationResult
    data object TagViolation : ClanValidationResult
}