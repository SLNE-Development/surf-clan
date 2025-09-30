package dev.slne.surf.clan.core.client.clan

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.core.common.clan.ClanCommon
import dev.slne.surf.clan.core.common.clan.ClanManagerCommon
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.ServerboundAllClansPacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.member.ServerboundAddMemberPacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.member.ServerboundInviteMemberPacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.member.ServerboundRemoveMemberPacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.member.ServerboundUninviteMemberPacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.options.ServerboundSetClanDiscordInvitePacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.options.ServerboundSetClanNamePacket
import dev.slne.surf.clan.core.common.netty.protocol.serverbound.options.ServerboundSetClanTagPacket
import dev.slne.surf.cloud.api.client.netty.packet.fireAndAwaitOrThrow
import dev.slne.surf.cloud.api.common.util.toObjectSet
import org.springframework.stereotype.Component

@Component
class ClanManagerClient : ClanManagerCommon() {
    override suspend fun findAllClans() = ServerboundAllClansPacket().fireAndAwaitOrThrow().clans
        .map { it as ClanCommon }
        .toObjectSet()

    override suspend fun addMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer,
        role: ClanMemberRole
    ) = ServerboundAddMemberPacket(
        clanUuid = clan.uuid,
        playerUuid = player.uuid,
        targetUuid = target.uuid,
        role
    ).fireAndAwaitOrThrow().result

    override suspend fun inviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ) = ServerboundInviteMemberPacket(
        clanUuid = clan.uuid,
        playerUuid = player.uuid,
        targetUuid = target.uuid
    ).fireAndAwaitOrThrow().result

    override suspend fun uninviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ) = ServerboundUninviteMemberPacket(
        clanUuid = clan.uuid,
        playerUuid = player.uuid,
        targetUuid = target.uuid
    ).fireAndAwaitOrThrow().result

    override suspend fun removeMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ) = ServerboundRemoveMemberPacket(
        clan.uuid,
        player.uuid,
        target.uuid
    ).fireAndAwaitOrThrow().result

    override suspend fun setName(
        clan: Clan,
        player: ClanPlayer,
        name: String
    ) = ServerboundSetClanNamePacket(
        clanUuid = clan.uuid,
        playerUuid = player.uuid,
        name = name
    ).fireAndAwaitOrThrow().result

    override suspend fun setTag(
        clan: Clan,
        player: ClanPlayer,
        tag: ClanTag
    ) = ServerboundSetClanTagPacket(
        clanUuid = clan.uuid,
        playerUuid = player.uuid,
        clanTag = tag
    ).fireAndAwaitOrThrow().result

    override suspend fun setDiscordInvite(
        clan: Clan,
        player: ClanPlayer,
        invite: String?
    ) = ServerboundSetClanDiscordInvitePacket(
        clanUuid = clan.uuid,
        playerUuid = player.uuid,
        invite = invite
    ).fireAndAwaitOrThrow().result
}