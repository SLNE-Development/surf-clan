package dev.slne.clan.api.invite

sealed interface ClanInviteResult {
    data class Success(val invite: ClanInvite) : ClanInviteResult
    data object AlreadyInvited : ClanInviteResult
    data object AlreadyInClan : ClanInviteResult
    data object InvitationsDisabled : ClanInviteResult
}