package dev.slne.surf.clan.api.common.results

enum class ClanCreateResult {

    /**
     * The clan was created successfully.
     */
    SUCCESS,

    /**
     * The clan name is already taken.
     */
    NAME_TAKEN,

    /**
     * The clan tag is already taken.
     */
    TAG_TAKEN,

    /**
     * The clan name is too short or too long.
     */
    NAME_LENGTH,

    /**
     * The clan tag is too short or too long.
     */
    TAG_LENGTH,

    /**
     * The player is already in a clan.
     */
    ALREADY_IN_CLAN

}