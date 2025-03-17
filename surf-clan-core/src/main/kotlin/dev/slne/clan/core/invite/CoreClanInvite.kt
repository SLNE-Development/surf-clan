package dev.slne.clan.core.invite

import dev.slne.clan.api.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.player.ClanPlayer
import java.time.LocalDateTime

class CoreClanInvite(
    override val invited: ClanPlayer,
    override val invitedBy: ClanPlayer,

    override val clan: Clan,

    override val createdAt: LocalDateTime? = LocalDateTime.now(),
    override var updatedAt: LocalDateTime? = LocalDateTime.now(),
) : ClanInvite {

    override fun toString(): String {
        return "CoreClanInvite(invited=$invited, invitedBy=$invitedBy, createdAt=$createdAt, updatedAt=$updatedAt)"
    }
}