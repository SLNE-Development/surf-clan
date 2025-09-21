package dev.slne.surf.clan.api.common.clan.actions

import dev.slne.surf.clan.api.common.util.ClanAction

data class ChangeDiscordInviteArguments(
    val oldInvite: String?,
    val newInvite: String?
)

interface ChangeDiscordInviteAction : ClanAction<ChangeDiscordInviteArguments>