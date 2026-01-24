package dev.slne.clan.api.clan

import it.unimi.dsi.fastutil.chars.Char2BooleanMap

/**
 * Represents the result of validating a clan name and tag.
 *
 * This sealed interface provides various validation outcomes, indicating whether
 * the proposed clan name and tag meet system requirements.
 *
 * @see Clan.validateClanNameAndTag
 */
sealed interface ClanValidationResult {
    /**
     * Indicates that both the clan name and tag are valid.
     */
    data object Valid : ClanValidationResult

    /**
     * Indicates that the clan name length is outside the allowed range.
     *
     * @property min the minimum allowed name length
     * @property max the maximum allowed name length
     */
    data class NameOutOfRange(val min: Int, val max: Int) : ClanValidationResult

    /**
     * Indicates that the clan tag length is outside the allowed range.
     *
     * @property min the minimum allowed tag length
     * @property max the maximum allowed tag length
     */
    data class TagOutOfRange(val min: Int, val max: Int) : ClanValidationResult

    /**
     * Indicates that the clan tag contains invalid characters.
     *
     * @property characters a map of characters to their validity status
     */
    data class InvalidTagCharacters(val characters: Char2BooleanMap) : ClanValidationResult

    /**
     * Indicates that the clan tag violates system rules (e.g., contains profanity
     * or reserved words).
     */
    data object TagViolation : ClanValidationResult
}