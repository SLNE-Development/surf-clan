package dev.slne.surf.clan.core.client.player

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.member.ServerboundSetMemberRolePacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.player.ServerboundFindClanPlayerPacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.player.options.ServerboundSetAcceptsClanInvitesPacket
import dev.slne.surf.clan.core.common.player.ClanPlayerManagerCommon
import dev.slne.surf.cloud.api.client.netty.packet.fireAndAwaitOrThrow
import org.springframework.stereotype.Component
import java.util.*

@Component
class ClanPlayerManagerClient : ClanPlayerManagerCommon() {
    override suspend fun findOrCreatePlayer(uuid: UUID) =
        ServerboundFindClanPlayerPacket(uuid).fireAndAwaitOrThrow().clanPlayer

    override suspend fun setMemberRole(
        clan: Clan,
        member: ClanMember,
        role: ClanMemberRole,
        setBy: ClanPlayer
    ) = ServerboundSetMemberRolePacket(
        clan.uuid,
        member.uuid,
        setBy.uuid,
        role
    ).fireAndAwaitOrThrow().result

    override suspend fun setAcceptsClanInvites(
        player: ClanPlayer,
        value: Boolean
    ) = ServerboundSetAcceptsClanInvitesPacket(player.uuid, value).fireAndAwaitOrThrow().value
}