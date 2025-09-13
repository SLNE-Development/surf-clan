package dev.slne.surf.clan.core.common.clan.invite

import dev.slne.surf.clan.api.common.clan.invite.ClanInvite
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import java.time.ZonedDateTime
import java.util.*

@Serializable
class ClanInviteCommon(
    override val invitedUuid: @Contextual UUID,
    override val invitedByUuid: @Contextual UUID,
    override val createdAt: @Contextual ZonedDateTime,
    override val updatedAt: @Contextual ZonedDateTime,
) : ClanInvite