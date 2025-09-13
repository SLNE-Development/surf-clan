package dev.slne.surf.clan.core.client.clan

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
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
import org.springframework.stereotype.Component

@Component
class ClanManagerClient : ClanManagerCommon() {
    override suspend fun findAllClans() =
        ServerboundAllClansPacket().fireAndAwaitOrThrow().clans

    override suspend fun inviteMember(
        clan: Clan,
        player: ClanPlayer,
        invitedBy: ClanPlayer
    ) = ServerboundInviteMemberPacket(
        clan.uuid,
        player.uuid,
        invitedBy.uuid
    ).fireAndAwaitOrThrow().result

    override suspend fun uninviteMember(
        clan: Clan,
        player: ClanPlayer,
        uninvitedBy: ClanPlayer
    ) = ServerboundUninviteMemberPacket(
        clan.uuid,
        player.uuid,
        uninvitedBy.uuid
    ).fireAndAwaitOrThrow().result

    override suspend fun addMember(
        clan: Clan,
        player: ClanPlayer,
        role: ClanMemberRole,
        addedBy: ClanPlayer
    ) = ServerboundAddMemberPacket(
        clan.uuid,
        player.uuid,
        addedBy.uuid,
        role
    ).fireAndAwaitOrThrow().result

    override suspend fun removeMember(
        clan: Clan,
        clanPlayer: ClanPlayer,
        removedBy: ClanPlayer
    ) = ServerboundRemoveMemberPacket(
        clan.uuid,
        clanPlayer.uuid,
        removedBy.uuid
    ).fireAndAwaitOrThrow().result

    override suspend fun setName(
        clan: Clan,
        name: String,
        setBy: ClanPlayer
    ) = ServerboundSetClanNamePacket(
        clan.uuid,
        name,
        setBy.uuid
    ).fireAndAwaitOrThrow().result

    override suspend fun setTag(
        clan: Clan,
        tag: ClanTag,
        setBy: ClanPlayer
    ) = ServerboundSetClanTagPacket(
        clan.uuid,
        tag,
        setBy.uuid
    ).fireAndAwaitOrThrow().result

    override suspend fun setDiscordInvite(
        clan: Clan,
        discordInvite: String?,
        setBy: ClanPlayer
    ) = ServerboundSetClanDiscordInvitePacket(
        clan.uuid,
        discordInvite
    ).fireAndAwaitOrThrow().result
}