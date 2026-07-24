package dev.slne.surf.clan.core.rpc

import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.api.clan.update.ClanNameAndTag
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.rabbitmq.api.rpc.RpcService
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import java.util.*

@RpcService
interface ClanRpcService {

    suspend fun createClan(
        name: String,
        tag: String,
        owner: UUID,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?,
        description: String?,
        discordInvite: String?
    ): ClanCreationResult

    suspend fun deleteClan(clanID: ULong): Boolean

    suspend fun findAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl>
    suspend fun findClanById(clanID: ULong): ClanImpl?
    suspend fun findClanByMember(memberUuid: UUID): ClanImpl?
    suspend fun findClanByTag(tag: String): ClanImpl?
    suspend fun findClanByName(name: String): ClanImpl?
    suspend fun findClanByClanUuid(clanUuid: UUID): ClanImpl?
    suspend fun findClanTagsByPrefix(prefix: String, limit: Int): List<String>

    suspend fun updateClanDescription(clanID: ULong, description: String?): Boolean
    suspend fun updateClanDiscordInvite(clanID: ULong, discordInvite: String?): Boolean
    suspend fun updateClanTagColor(
        clanID: ULong,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?
    ): Boolean

    suspend fun updateClanNameAndTag(
        clanID: ULong,
        name: String?,
        tag: String?
    ): ClanNameAndTag.UpdateResult
}