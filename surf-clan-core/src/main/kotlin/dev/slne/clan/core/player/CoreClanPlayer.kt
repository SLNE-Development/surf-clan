package dev.slne.clan.core.player

import dev.slne.clan.api.player.ClanPlayer
import java.util.*

class CoreClanPlayer(
    override val uuid: UUID,
    override var username: String? = null,
    override var acceptsClanInvites: Boolean = true
) : ClanPlayer {
    override fun toString(): String {
        return "CoreClanPlayer(uuid=$uuid, username=$username, acceptsClanInvites=$acceptsClanInvites)"
    }
}