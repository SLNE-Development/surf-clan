package dev.slne.surf.clan.server.clan

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberAddResult
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberRemoveResult
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberInviteResult
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.result.ClanSetDiscordInviteResult
import dev.slne.surf.clan.api.common.clan.result.ClanSetNameResult
import dev.slne.surf.clan.api.common.clan.result.ClanSetTagResult
import dev.slne.surf.clan.api.common.clan.tag.ClanTag
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.server.db.entities.ClanEntity
import dev.slne.surf.clan.server.db.entities.ClanInviteEntity
import dev.slne.surf.clan.server.db.entities.ClanMemberEntity
import dev.slne.surf.clan.server.db.tables.ClanInvitesTable
import dev.slne.surf.clan.server.db.tables.ClansTable
import dev.slne.surf.clan.server.player.ClanPlayerRepository
import dev.slne.surf.cloud.api.server.plugin.CoroutineTransactional
import org.jetbrains.exposed.sql.and
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CoroutineTransactional
class ClanRepository(private val clanPlayerRepository: ClanPlayerRepository) {
    suspend fun findClanByUuid(uuid: UUID) = ClanEntity.find {
        ClansTable.uuid eq uuid
    }.firstOrNull()

    suspend fun findAllClans() = ClanEntity.all().map { it.toDto() }

    suspend fun inviteMember(
        clan: Clan,
        player: ClanPlayer,
        invitedBy: ClanPlayer
    ): ClanMemberInviteResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ClanMemberInviteResult.ClanNotFound(clan)

        val playerEntity = clanPlayerRepository.findOrCreatePlayerRaw(player.uuid)
        val invitedByEntity = clanPlayerRepository.findOrCreatePlayerRaw(invitedBy.uuid)

        ClanInviteEntity.new {
            this.clan = clanEntity
            this.invited = playerEntity
            this.invitedBy = invitedByEntity
        }

        return ClanMemberInviteResult.Success(clan, player.uuid)
    }

    suspend fun uninviteMember(
        clan: Clan,
        player: ClanPlayer,
    ): ClanMemberUninviteResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ClanMemberUninviteResult.ClanNotFound(clan)

        val playerEntity = clanPlayerRepository.findOrCreatePlayerRaw(player.uuid)

        val inviteEntity = ClanInviteEntity.find {
            (ClanInvitesTable.clan eq clanEntity.id) and
                    (ClanInvitesTable.invited eq playerEntity.id)
        }.firstOrNull() ?: return ClanMemberUninviteResult.NotInvited(clan, player.uuid)

        inviteEntity.delete()

        return ClanMemberUninviteResult.Success(clan, player.uuid)
    }

    suspend fun addMember(
        clan: Clan,
        player: ClanPlayer,
        addedBy: ClanPlayer,
        role: ClanMemberRole
    ): ClanMemberAddResult {
        val clanEntity = findClanByUuid(clan.uuid) ?: return ClanMemberAddResult.ClanNotFound(clan)

        val playerEntity = clanPlayerRepository.findOrCreatePlayerRaw(player.uuid)
        val addedByEntity = clanPlayerRepository.findOrCreatePlayerRaw(addedBy.uuid)

        ClanMemberEntity.new {
            this.clan = clanEntity
            this.player = playerEntity
            this.addedBy = addedByEntity
            this.role = role
        }

        return ClanMemberAddResult.Success(clan, player.uuid)
    }

    suspend fun removeMember(
        clan: Clan,
        player: ClanPlayer
    ): ClanMemberRemoveResult {
        val clanEntity =
            findClanByUuid(clan.uuid) ?: return ClanMemberRemoveResult.ClanNotFound(clan)

        val playerEntity = clanPlayerRepository.findOrCreatePlayerRaw(player.uuid)

        val memberEntity = ClanMemberEntity.find {
            (ClanInvitesTable.clan eq clanEntity.id) and
                    (ClanInvitesTable.invited eq playerEntity.id)
        }.firstOrNull() ?: return ClanMemberRemoveResult.NotClanMember(clan, player.uuid)

        memberEntity.delete()

        return ClanMemberRemoveResult.Success(clan, player.uuid)
    }

    suspend fun setDiscordInvite(
        clan: Clan,
        discordInvite: String?
    ): ClanSetDiscordInviteResult {
        val clanEntity =
            findClanByUuid(clan.uuid) ?: return ClanSetDiscordInviteResult.ClanNotFound(clan)

        clanEntity.discordInvite = discordInvite

        return ClanSetDiscordInviteResult.Success(clan, discordInvite)
    }

    suspend fun setName(
        clan: Clan,
        name: String
    ): ClanSetNameResult {
        val clanEntity = findClanByUuid(clan.uuid) ?: return ClanSetNameResult.ClanNotFound(clan)

        val oldName = clan.name
        clanEntity.name = name

        return ClanSetNameResult.Success(clan, oldName, name)
    }

    suspend fun setTag(
        clan: Clan,
        tag: ClanTag
    ): ClanSetTagResult {
        val clanEntity = findClanByUuid(clan.uuid) ?: return ClanSetTagResult.ClanNotFound(clan)

        val oldTag = clan.fullTag
        clanEntity.clanTag = tag

        return ClanSetTagResult.Success(clan, oldTag, tag)
    }
}