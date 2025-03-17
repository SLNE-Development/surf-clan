package dev.slne.clan.api.invite

import dev.slne.clan.api.player.ClanPlayer
import java.time.LocalDateTime

interface ClanInvite {

    val invited: ClanPlayer
    val invitedBy: ClanPlayer

    val createdAt: LocalDateTime?
    val updatedAt: LocalDateTime?
}