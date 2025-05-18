package dev.slne.surf.clan.api.common.player

import java.util.*

interface ClanPlayer {

    /**
     * The unique identifier of the player.
     */
    val uuid: UUID

    /**
     * If the player accepts clan invites.
     */
    var acceptsClanInvites: Boolean
}