package dev.slne.surf.clan.microservice.rpc

import dev.slne.clan.api.clan.ClanCreationResult
import dev.slne.clan.api.clan.update.ClanNameAndTag
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.rpc.ClanRpcService
import dev.slne.surf.clan.microservice.db.repository.ClanRepository
import net.kyori.adventure.text.format.ShadowColor
import net.kyori.adventure.text.format.TextColor
import java.util.*

object ClanRpcServiceImpl : ClanRpcService {
    override suspend fun createClan(
        name: String,
        tag: String,
        owner: UUID,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?,
        description: String?,
        discordInvite: String?
    ): ClanCreationResult {
        return ClanRepository.create(
            name = name,
            tag = tag,
            owner = owner,
            tagForegroundColor = tagForegroundColor,
            tagBackgroundColor = tagBackgroundColor,
            tagShadowColor = tagShadowColor,
            description = description,
            discordInvite = discordInvite
        )
    }

    override suspend fun deleteClan(clanID: ULong): Boolean {
        return ClanRepository.delete(clanID)
    }

    override suspend fun findAllClansWithoutMembersSortByMemberCount(): Collection<ClanImpl> {
        return ClanRepository.fetchAllClansWithoutMembersSortByMemberCount()
    }

    override suspend fun findClanById(clanID: ULong): ClanImpl? {
        return ClanRepository.findClanByID(clanID)
    }

    override suspend fun findClanByMember(memberUuid: UUID): ClanImpl? {
        return ClanRepository.findClanByPlayer(memberUuid)
    }

    override suspend fun findClanByTag(tag: String): ClanImpl? {
        return ClanRepository.findClanByTag(tag)
    }

    override suspend fun findClanByClanUuid(clanUuid: UUID): ClanImpl? {
        return ClanRepository.findClanByUuid(clanUuid)
    }

    override suspend fun findClanTagsByPrefix(
        prefix: String,
        limit: Int
    ): List<String> {
        return ClanRepository.suggestTagsByPrefix(prefix, limit)
    }

    override suspend fun updateClanDescription(clanID: ULong, description: String?): Boolean {
        return ClanRepository.updateDescription(clanID, description)
    }

    override suspend fun updateClanDiscordInvite(clanID: ULong, discordInvite: String?): Boolean {
        return ClanRepository.updateDiscordInvite(clanID, discordInvite)
    }

    override suspend fun updateClanTagColor(
        clanID: ULong,
        tagForegroundColor: TextColor?,
        tagBackgroundColor: TextColor?,
        tagShadowColor: ShadowColor?
    ): Boolean {
        return ClanRepository.updateTagColor(
            clanID = clanID,
            tagForegroundColor = tagForegroundColor,
            tagBackgroundColor = tagBackgroundColor,
            tagShadowColor = tagShadowColor
        )
    }

    override suspend fun updateClanNameAndTag(clanID: ULong, name: String?, tag: String?): ClanNameAndTag.UpdateResult {
        return ClanRepository.updateClanNameAndTag(clanID, name, tag)
    }
}