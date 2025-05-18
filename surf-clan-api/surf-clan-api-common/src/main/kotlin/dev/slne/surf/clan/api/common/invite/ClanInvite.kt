package dev.slne.surf.clan.api.common.invite

import dev.slne.surf.clan.api.common.member.ClanMember
import java.time.ZonedDateTime
import java.util.*

interface ClanInvite {

    /**
     * Unique identifier of the invited user
     */
    val invited: UUID

    /**
     * The clan member who invited the user
     */
    val invitedBy: ClanMember?

    /**
     * Unique identifier of the clan member who invited the user
     */
    val invitedByUuid: UUID

    /**
     * Timestamp at which the invite was created
     */
    val createdAt: ZonedDateTime?

    /**
     * Timestamp at which the invite was updated
     */
    val updatedAt: ZonedDateTime?
}