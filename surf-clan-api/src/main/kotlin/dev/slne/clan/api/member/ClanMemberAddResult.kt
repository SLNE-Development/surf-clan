package dev.slne.clan.api.member

sealed interface ClanMemberAddResult {
    data class Success(val member: ClanMember) : ClanMemberAddResult
    data object AlreadyMember : ClanMemberAddResult
}