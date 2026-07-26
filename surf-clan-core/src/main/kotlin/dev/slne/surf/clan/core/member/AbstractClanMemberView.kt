package dev.slne.surf.clan.core.member

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMemberView
import dev.slne.clan.api.permission.ClanPermission
import java.time.OffsetDateTime

abstract class AbstractClanMemberView : ClanMemberView {

    override fun isActiveAt(now: OffsetDateTime): Boolean {
        return !lastActiveAt.isBefore(now.minusSeconds(Clan.INACTIVE_AFTER.inWholeSeconds))
    }

    override fun hasPermission(clanPermission: ClanPermission): Boolean {
        return role.hasPermission(clanPermission)
    }
}