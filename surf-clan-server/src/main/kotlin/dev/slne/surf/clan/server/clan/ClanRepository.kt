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
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.core.common.clan.ClanCommon
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

    suspend fun findAllClans(): List<ClanCommon> {
        println("1")
        return ClanEntity.all()
            .map {
                println("2")
                it.toDto()
            }
    }

    suspend fun inviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ComponentResult.ClanNotFound(clan)

        val playerEntity = clanPlayerRepository.findOrCreatePlayerRaw(player.uuid)
        val targetEntity = clanPlayerRepository.findOrCreatePlayerRaw(target.uuid)

        ClanInviteEntity.new {
            this.clan = clanEntity
            this.invitedBy = playerEntity
            this.invited = targetEntity
        }

        return ClanMemberInviteResult.Success(
            clan = clan,
            playerUuid = player.uuid,
            targetUuid = target.uuid
        )
    }

    suspend fun uninviteMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer,
    ): ComponentResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ComponentResult.ClanNotFound(clan)

        val playerEntity = clanPlayerRepository.findOrCreatePlayerRaw(player.uuid)

        val inviteEntity = ClanInviteEntity.find {
            (ClanInvitesTable.clan eq clanEntity.id) and
                    (ClanInvitesTable.invited eq playerEntity.id)
        }.firstOrNull() ?: return ClanMemberUninviteResult.NotInvited(
            clan = clan,
            playerUuid = player.uuid,
            targetUuid = target.uuid
        )

        inviteEntity.delete()

        return ClanMemberUninviteResult.Success(
            clan = clan,
            playerUuid = player.uuid,
            targetUuid = target.uuid
        )
    }

    suspend fun addMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer,
        role: ClanMemberRole
    ): ComponentResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ComponentResult.ClanNotFound(clan)

        val playerEntity = clanPlayerRepository.findOrCreatePlayerRaw(player.uuid)
        val targetEntity = clanPlayerRepository.findOrCreatePlayerRaw(target.uuid)

        ClanMemberEntity.new {
            this.clan = clanEntity
            this.player = targetEntity
            this.addedBy = playerEntity
            this.role = role
        }

        return ClanMemberAddResult.Success(
            clan = clan,
            playerUuid = player.uuid,
            targetUuid = target.uuid
        )
    }

    suspend fun removeMember(
        clan: Clan,
        player: ClanPlayer,
        target: ClanPlayer
    ): ComponentResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ComponentResult.ClanNotFound(clan)

        val targetEntity = clanPlayerRepository.findOrCreatePlayerRaw(target.uuid)

        val memberEntity = ClanMemberEntity.find {
            (ClanInvitesTable.clan eq clanEntity.id) and
                    (ClanInvitesTable.invited eq targetEntity.id)
        }.firstOrNull() ?: return ComponentResult.OtherNotClanMember(clan, player.uuid, target.uuid)

        memberEntity.delete()

        return ClanMemberRemoveResult.Success(
            clan = clan, playerUuid = player.uuid, targetUuid = target.uuid
        )
    }

    suspend fun setDiscordInvite(
        clan: Clan,
        player: ClanPlayer,
        invite: String?
    ): ComponentResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ComponentResult.ClanNotFound(clan)

        clanEntity.discordInvite = invite

        return ClanSetDiscordInviteResult.Success(
            clan = clan,
            playerUuid = player.uuid,
            discordInvite = invite
        )
    }

    suspend fun setName(
        clan: Clan,
        player: ClanPlayer,
        name: String
    ): ComponentResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ComponentResult.ClanNotFound(clan)

        val oldName = clan.name
        clanEntity.name = name

        return ClanSetNameResult.Success(
            clan = clan,
            playerUuid = player.uuid,
            oldName = oldName,
            newName = name
        )
    }

    suspend fun setTag(
        clan: Clan,
        player: ClanPlayer,
        tag: ClanTag
    ): ComponentResult {
        val clanEntity = findClanByUuid(clan.uuid)
            ?: return ComponentResult.ClanNotFound(clan)

        val oldTag = clan.fullTag
        clanEntity.clanTag = tag

        return ClanSetTagResult.Success(
            clan = clan,
            playerUuid = player.uuid,
            oldTag = oldTag,
            newTag = tag
        )
    }
}