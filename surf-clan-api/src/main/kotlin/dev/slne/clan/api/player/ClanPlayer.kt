package dev.slne.clan.api.player

import java.util.*

data class ClanPlayer(
    val uuid: UUID,
    var username: String,
    var acceptsClanInvites: Boolean = true
)