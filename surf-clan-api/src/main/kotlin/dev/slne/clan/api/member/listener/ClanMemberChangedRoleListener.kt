package dev.slne.clan.api.member.listener

import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole

fun interface ClanMemberChangedRoleListener : ClanMemberListener {
    fun onClanMemberChangedRole(member: ClanMember, newRole: ClanMemberRole)
}