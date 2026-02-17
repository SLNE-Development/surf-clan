package dev.slne.clan.core.member

import dev.slne.clan.api.member.ClanMemberView
import dev.slne.clan.api.permission.ClanPermission

abstract class AbstractClanMemberView : ClanMemberView {
    override fun hasPermission(clanPermission: ClanPermission): Boolean {
        return role.hasPermission(clanPermission)
    }
}