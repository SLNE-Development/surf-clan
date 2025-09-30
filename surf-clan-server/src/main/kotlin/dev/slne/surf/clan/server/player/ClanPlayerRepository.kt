package dev.slne.surf.clan.server.player

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.result.role.ClanMemberSetRoleResult
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ComponentResult
import dev.slne.surf.clan.server.db.entities.ClanEntity
import dev.slne.surf.clan.server.db.entities.ClanMemberEntity
import dev.slne.surf.clan.server.db.entities.ClanPlayerEntity
import dev.slne.surf.clan.server.db.tables.ClanMembersTable
import dev.slne.surf.clan.server.db.tables.ClanPlayersTable
import dev.slne.surf.cloud.api.server.plugin.CoroutineTransactional
import org.jetbrains.exposed.sql.and
import org.jetbrains.exposed.sql.update
import org.jetbrains.exposed.sql.upsertReturning
import org.springframework.stereotype.Repository
import java.util.*

@Repository
@CoroutineTransactional
class ClanPlayerRepository {
    suspend fun findOrCreatePlayerRaw(uuid: UUID) =
        ClanPlayersTable.upsertReturning(where = { ClanPlayersTable.uuid eq uuid }) {
            it[ClanPlayersTable.uuid] = uuid
        }.single().let { ClanPlayerEntity.wrapRow(it) }

    suspend fun findOrCreatePlayer(uuid: UUID) =
        findOrCreatePlayerRaw(uuid).toDto()

    suspend fun setAcceptsClanInvites(player: ClanPlayer, value: Boolean): Boolean =
        ClanPlayersTable.update({ ClanPlayersTable.uuid eq player.uuid }) {
            it[acceptsClanInvites] = value
        } > 0

    suspend fun setMemberRole(
        clan: Clan,
        player: ClanPlayer,
        target: ClanMember,
        role: ClanMemberRole
    ): ComponentResult {
        val oldRole = target.role

        val targetPlayer = target.clanPlayer()
        val targetEntity = ClanPlayerEntity.find { ClanPlayersTable.uuid eq target.uuid }
            .firstOrNull() ?: return ComponentResult.PlayerNotFound(targetPlayer.uuid)

        val clanEntity = ClanEntity.find { ClanPlayersTable.uuid eq clan.uuid }
            .firstOrNull() ?: return ComponentResult.ClanNotFound(clan)

        val memberEntity = ClanMemberEntity.find {
            (ClanMembersTable.clan eq clanEntity.id) and (ClanMembersTable.player eq targetEntity.id)
        }.firstOrNull() ?: return ComponentResult.OtherNotClanMember(clan, player.uuid, target.uuid)

        memberEntity.role = role

        return ClanMemberSetRoleResult.Success(clan, target, oldRole, role)
    }
}