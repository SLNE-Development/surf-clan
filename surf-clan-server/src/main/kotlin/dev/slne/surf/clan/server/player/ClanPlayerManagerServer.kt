package dev.slne.surf.clan.server.player

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.core.common.player.ClanPlayerManagerCommon
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClanPlayerManagerServer(
    private val clanPlayerRepository: ClanPlayerRepository
) : ClanPlayerManagerCommon() {
    override suspend fun findOrCreatePlayer(uuid: UUID) =
        clanPlayerRepository.findOrCreatePlayer(uuid)

    override suspend fun setMemberRole(
        clan: Clan,
        player: ClanPlayer,
        targetMember: ClanMember,
        role: ClanMemberRole,
    ) = clanPlayerRepository.setMemberRole(clan, player, targetMember, role)

    override suspend fun setAcceptsClanInvites(
        player: ClanPlayer,
        value: Boolean
    ) = clanPlayerRepository.setAcceptsClanInvites(player, value)
}