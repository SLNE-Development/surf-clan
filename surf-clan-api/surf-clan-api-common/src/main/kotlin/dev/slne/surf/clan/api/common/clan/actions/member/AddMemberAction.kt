package dev.slne.surf.clan.api.common.clan.actions.member

import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction

data class AddMemberArguments(
    val target: ClanPlayer
)

interface AddMemberAction : ClanAction<AddMemberArguments>