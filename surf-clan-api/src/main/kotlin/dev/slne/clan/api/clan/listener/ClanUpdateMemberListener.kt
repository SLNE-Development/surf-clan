package dev.slne.clan.api.clan.listener

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMember
import java.util.UUID

fun interface ClanUpdateMemberListener : ClanListener {
    fun onClanMemberUpdated(clan: Clan, memberUuid: UUID, added: Boolean)
}