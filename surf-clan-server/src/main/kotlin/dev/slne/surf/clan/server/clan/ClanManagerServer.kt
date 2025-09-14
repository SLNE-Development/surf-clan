package dev.slne.surf.clan.server.clan

import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MAX_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_NAME_MIN_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_TAG_MAX_LENGTH
import dev.slne.surf.clan.api.common.clan.CLAN_TAG_MIN_LENGTH
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberAddResult
import dev.slne.surf.clan.api.common.clan.member.result.ClanMemberRemoveResult
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberInviteResult
import dev.slne.surf.clan.api.common.clan.member.result.invite.ClanMemberUninviteResult
import dev.slne.surf.clan.api.common.clan.member.role.ClanMemberRole
import dev.slne.surf.clan.api.common.clan.member.role.permission.ClanPermission
import dev.slne.surf.clan.api.common.clan.result.ClanSetDiscordInviteResult
import dev.slne.surf.clan.api.common.clan.result.ClanSetNameResult
import dev.slne.surf.clan.api.common.clan.result.ClanSetTagResult
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
        invitedBy: ClanPlayer
    ): ClanMemberInviteResult {
        if (clan.isMember(player)) {
            return ClanMemberInviteResult.AlreadyMember(clan, player.uuid)
        }

        val invitedByMember =
            clan.getMember(invitedBy) ?: return ClanMemberInviteResult.NotClanMember(clan)

        if (!invitedByMember.hasPermission(ClanPermission.INVITE)) {
            return ClanMemberInviteResult.NoPermissions(clan)
        }

        return clanRepository.inviteMember(clan, player, invitedBy)
    }

    override suspend fun uninviteMember(
        clan: Clan,
        player: ClanPlayer,
        uninvitedBy: ClanPlayer
    ): ClanMemberUninviteResult {
        if (!clan.isInvited(player)) {
            return ClanMemberUninviteResult.NotInvited(clan, player.uuid)
        }

        val uninvitedByMember =
            clan.getMember(uninvitedBy) ?: return ClanMemberUninviteResult.NotClanMember(clan)

        if (!uninvitedByMember.hasPermission(ClanPermission.INVITE)) {
            return ClanMemberUninviteResult.NoPermissions(clan)
        }

        return clanRepository.uninviteMember(clan, player)
    }

    override suspend fun addMember(
        clan: Clan,
        player: ClanPlayer,
        role: ClanMemberRole,
        addedBy: ClanPlayer
    ): ClanMemberAddResult {
        val uninviteResult = clanRepository.uninviteMember(clan, player)

        if (clan.isMember(player)) {
            return ClanMemberAddResult.AlreadyMember(clan, player.uuid)
        }

        if (!uninviteResult.isSuccess) {
            return ClanMemberAddResult.InviteRemoveFailed(clan, player.uuid, uninviteResult)
        }

        return clanRepository.addMember(clan, player, addedBy, role)
    }

    override suspend fun removeMember(
        clan: Clan,
        clanPlayer: ClanPlayer,
        removedBy: ClanPlayer
    ): ClanMemberRemoveResult {
        val member = clan.getMember(clanPlayer)
            ?: return ClanMemberRemoveResult.NotClanMember(clan, clanPlayer.uuid)

        val removedByMember = clan.getMember(removedBy)
            ?: return ClanMemberRemoveResult.SelfNotClanMember(clan)

        if (!removedByMember.hasPermission(ClanPermission.KICK)) {
            return ClanMemberRemoveResult.NoPermission(clan)
        }

        if (member.role.isOwner) {
            return ClanMemberRemoveResult.OwnerCannotBeRemoved(clan)
        }

        if (clanPlayer == removedBy) {
            return ClanMemberRemoveResult.CannotKickYourself(clan)
        }

        if (removedByMember.role <= member.role) {
            return ClanMemberRemoveResult.CannotKickSameOrHigherRole(clan)
        }

        return clanRepository.removeMember(clan, clanPlayer)
    }

    override suspend fun setName(
        clan: Clan,
        name: String,
        setBy: ClanPlayer
    ): ClanSetNameResult {
        val member = clan.getMember(setBy) ?: return ClanSetNameResult.NotClanMember(clan)

        if (!member.hasPermission(ClanPermission.OPTIONS_NAME)) {
            return ClanSetNameResult.NoPermission(clan)
        }

        if (clans.any { it.name.equals(name, true) }) {
            return ClanSetNameResult.NameAlreadyInUse(name)
        }

        if (name.length !in CLAN_NAME_MIN_LENGTH..CLAN_NAME_MAX_LENGTH) {
            return ClanSetNameResult.NameDoesntMatchLength(
                name,
                CLAN_NAME_MIN_LENGTH,
                CLAN_NAME_MAX_LENGTH
            )
        }

        return clanRepository.setName(clan, name)
    }

    override suspend fun setTag(
        clan: Clan,
        tag: ClanTag,
        setBy: ClanPlayer
    ): ClanSetTagResult {
        val member = clan.getMember(setBy) ?: return ClanSetTagResult.NotClanMember(clan)

        if (!member.hasPermission(ClanPermission.OPTIONS_TAG)) {
            return ClanSetTagResult.NoPermission(clan)
        }

        if (clans.any { it.fullTag.tag.equals(tag.tag, true) }) {
            return ClanSetTagResult.TagAlreadyInUse(tag.tag)
        }

        if (tag.tag.length !in CLAN_TAG_MIN_LENGTH..CLAN_TAG_MAX_LENGTH) {
            return ClanSetTagResult.TagDoesntMatchLength(
                tag.tag,
                CLAN_TAG_MIN_LENGTH,
                CLAN_TAG_MAX_LENGTH
            )
        }

        // TODO: 13.09.2025 22:52 Blacklist checks ClanTagSetResult.InvalidTag

        return clanRepository.setTag(clan, tag)
    }

    private val discordInviteLinkPattern = Regex(
        pattern = """^(https?://)?(www\.)?(discord\.gg|discordapp\.com/invite)/[A-Za-z0-9]+/?$""",
        options = setOf(RegexOption.IGNORE_CASE)
    )

    override suspend fun setDiscordInvite(
        clan: Clan,
        discordInvite: String?,
        setBy: ClanPlayer
    ): ClanSetDiscordInviteResult {
        val member = clan.getMember(setBy) ?: return ClanSetDiscordInviteResult.NotClanMember(clan)

        if (!member.hasPermission(ClanPermission.OPTIONS_DISCORD)) {
            return ClanSetDiscordInviteResult.NoPermission(clan)
        }

        if (discordInvite != null && (discordInvite.length > 255 || !discordInviteLinkPattern.matches(
                discordInvite
            ))
        ) {
            return ClanSetDiscordInviteResult.InvalidDiscordInvite(discordInvite)
        }

        return clanRepository.setDiscordInvite(clan, discordInvite)
    }
}