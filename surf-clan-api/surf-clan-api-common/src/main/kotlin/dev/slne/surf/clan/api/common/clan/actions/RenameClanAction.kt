package dev.slne.surf.clan.api.common.clan.actions

import dev.slne.surf.clan.api.common.util.ClanAction

data class RenameClanArguments(
    val oldName: String,
    val newName: String
)

interface RenameClanAction : ClanAction<RenameClanArguments>