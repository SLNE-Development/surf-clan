package dev.slne.clan.api.invite

import dev.slne.clan.api.clan.Clan

sealed interface ClanInviteAcceptResult {
    data object AlreadyInClan : ClanInviteAcceptResult
    data class Accepted(val clan: Clan) : ClanInviteAcceptResult
}