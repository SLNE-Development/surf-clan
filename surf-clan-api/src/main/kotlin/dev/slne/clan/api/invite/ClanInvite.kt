package dev.slne.clan.api.invite

import dev.slne.clan.api.member.ClanMember
import java.time.LocalDateTime
import java.util.*

interface ClanInvite {

    val invited: UUID
    val invitedBy: ClanMember?
    val invitedByUuid: UUID

    val createdAt: LocalDateTime?
    val updatedAt: LocalDateTime?
}