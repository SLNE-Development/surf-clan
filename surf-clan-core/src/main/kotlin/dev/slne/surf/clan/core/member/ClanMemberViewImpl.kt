package dev.slne.surf.clan.core.member

import dev.slne.clan.api.member.ClanMemberRole
import java.util.*

data class ClanMemberViewImpl(
    override val uuid: UUID,
    override val role: ClanMemberRole,
    override val addedBy: UUID?
) : AbstractClanMemberView()