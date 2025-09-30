package dev.slne.surf.clan.server.clan

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.core.common.clan.ClanManagerCommon
import dev.slne.surf.surfapi.core.api.util.toObjectSet
import org.springframework.stereotype.Component

@Component
class ClanManagerServer(
    private val clanRepository: ClanRepository
) : ClanManagerCommon() {

    suspend fun cacheAllClans() {
        val clans = findAllClans()

        clearCache()
        addAllToCache(clans)
    }

    override suspend fun findAllClans() =
        clanRepository.findAllClans().toObjectSet()

    override suspend fun inviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ) = clanRepository.inviteMember(clan, player, target)

    override suspend fun uninviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ) = clanRepository.uninviteMember(clan, player, target)

    override suspend fun addMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer,
        role: ClanMemberRole
    ) = clanRepository.addMember(clan, player, target, role)

    override suspend fun removeMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ) = clanRepository.removeMember(clan, player, target)

    override suspend fun setName(
        clan: Clan,
        player: ClanPlayer,
        name: String
    ) = clanRepository.setName(clan, player, name)

    override suspend fun setTag(
        clan: Clan,
        player: ClanPlayer,
        tag: ClanTag
    ) = clanRepository.setTag(clan, player, tag)

    override suspend fun setDiscordInvite(
        clan: Clan,
        player: ClanPlayer,
        invite: String?
    ) = clanRepository.setDiscordInvite(clan, player, invite)
}