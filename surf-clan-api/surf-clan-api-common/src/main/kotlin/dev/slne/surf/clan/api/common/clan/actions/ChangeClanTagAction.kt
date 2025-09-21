package dev.slne.surf.clan.api.common.clan.actions

import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.util.ClanAction

data class ChangeClanTagArguments(
    val oldTag: ClanTag,
    val newTag: ClanTag
)

interface ChangeClanTagAction : ClanAction<ChangeClanTagArguments>