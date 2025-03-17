package dev.slne.clan.api.player

import java.util.*

interface ClanPlayer {

    val uuid: UUID
    var username: String?

    var acceptsClanInvites: Boolean

}