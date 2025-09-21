package dev.slne.surf.clan.api.common.clan.actions.member

import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction

class RemoveMemberArguments(
    target: ClanPlayer,
    targetMember: ClanMember
) : ClanAction.RequiredTargetActionArguments(target, targetMember)

interface RemoveMemberAction : ClanAction<RemoveMemberArguments>