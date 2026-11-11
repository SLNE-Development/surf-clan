package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.member.ClanMemberAddResult
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.api.core.service.PlayerLookupService
import dev.slne.surf.api.core.util.logger
import kotlinx.coroutines.CancellationException
import net.kyori.adventure.text.Component
import java.time.OffsetDateTime
import java.util.*

private val log = logger()

private val lookupName: suspend (UUID) -> String? = { PlayerLookupService.getUsername(it) }

const val NOT_A_MEMBER_OF_CLAN = "Der Spieler ist kein Mitglied dieses Clans."
const val NO_PENDING_INVITE_FOR_PLAYER = "Für diesen Spieler gibt es keine offene Einladung in diesen Clan."

/**
 * Who carries out an admin action. [uuid] is `null` when it comes from the console.
 */
data class ClanAdminActor(val name: String, val uuid: UUID?) {
    override fun toString() = "$name (${uuid ?: "console"})"
}

/**
 * The outcome of an admin action: a confirmation for the admin, or the reason it was refused.
 *
 * The actions return this instead of failing the command themselves, because each platform fails a
 * command its own way.
 */
sealed interface ClanAdminResult {
    data class Success(val message: Component) : ClanAdminResult
    data class Failure(val message: String) : ClanAdminResult
}

private fun ClanAdminError.asFailure() = ClanAdminResult.Failure(message)

private suspend fun (suspend (UUID) -> String?).displayName(uuid: UUID) = this(uuid) ?: uuid.toString()

/**
 * Gives the member of [clan] named by [target] the role [role], announcing it to the clan.
 */
suspend fun adminSetRole(
    actor: ClanAdminActor,
    clan: Clan,
    target: String,
    role: ClanMemberRole,
    nameOf: suspend (UUID) -> String? = lookupName,
    uuidOf: suspend (String) -> UUID? = lookupUuid
): ClanAdminResult {
    val member = findMemberOf(clan, target, uuidOf) ?: return ClanAdminResult.Failure(NOT_A_MEMBER_OF_CLAN)
    clan.setRoleError(member, role)?.let { return it.asFailure() }

    val oldRole = member.role
    if (!member.changeRole(role)) return ClanAdminResult.Failure(ROLE_CHANGE_FAILED)

    val memberName = nameOf.displayName(member.uuid)
    clan.broadcast(memberRoleSetByTeamMessage(memberName, oldRole, role))

    log.atInfo().log(
        "%s set the role of %s in clan %s (%s) from %s to %s",
        actor, member.uuid, clan.tag, clan.uuid, oldRole, role
    )

    return ClanAdminResult.Success(adminRoleSetMessage(memberName, clan.tag, oldRole, role))
}

/**
 * Makes the member of [clan] named by [target] its owner and the previous owner a leader.
 *
 * Without an atomic API this takes two role changes. The target is promoted first, so the clan briefly
 * has two owners but never none. Should the demotion fail, both stay owners and the admin is told how
 * to finish the transfer.
 */
suspend fun adminTransferOwner(
    actor: ClanAdminActor,
    clan: Clan,
    target: String,
    nameOf: suspend (UUID) -> String? = lookupName,
    uuidOf: suspend (String) -> UUID? = lookupUuid
): ClanAdminResult {
    val newOwner = findMemberOf(clan, target, uuidOf) ?: return ClanAdminResult.Failure(NOT_A_MEMBER_OF_CLAN)
    clan.transferOwnerError(newOwner)?.let { return it.asFailure() }
    val oldOwner = clan.owners().single()

    if (!newOwner.changeRole(ClanMemberRole.OWNER)) return ClanAdminResult.Failure(ROLE_CHANGE_FAILED)

    // The new owner is already stored, so a failing demotion must not abort silently: it is reported
    // like a refused one, leaving the admin to finish the transfer.
    val demoted = try {
        oldOwner.changeRole(ClanMemberRole.LEADER)
    } catch (e: CancellationException) {
        throw e
    } catch (e: Exception) {
        log.atWarning().withCause(e).log(
            "Could not demote the previous owner %s of clan %s (%s) after %s made %s owner",
            oldOwner.uuid, clan.tag, clan.uuid, actor, newOwner.uuid
        )
        false
    }

    val newOwnerName = nameOf.displayName(newOwner.uuid)
    val oldOwnerName = nameOf.displayName(oldOwner.uuid)
    clan.broadcast(ownershipTransferredByTeamMessage(newOwnerName, oldOwnerName, demoted))

    log.atInfo().log(
        "%s transferred ownership of clan %s (%s) from %s to %s (old owner demoted: %s)",
        actor, clan.tag, clan.uuid, oldOwner.uuid, newOwner.uuid, demoted
    )

    val hasCustomTagColor = clan.getClanTagColorOrDefault() != Clan.DEFAULT_CLAN_TAG_COLORS

    return ClanAdminResult.Success(
        adminOwnershipTransferredMessage(newOwnerName, oldOwnerName, clan.tag, demoted, hasCustomTagColor)
    )
}

/**
 * Removes the member of [clan] named by [target], announcing it to the clan.
 */
suspend fun adminKick(
    actor: ClanAdminActor,
    clan: Clan,
    target: String,
    nameOf: suspend (UUID) -> String? = lookupName,
    uuidOf: suspend (String) -> UUID? = lookupUuid
): ClanAdminResult {
    val member = findMemberOf(clan, target, uuidOf) ?: return ClanAdminResult.Failure(NOT_A_MEMBER_OF_CLAN)
    clan.kickError(member)?.let { return it.asFailure() }

    if (!clan.removeMember(member)) return ClanAdminResult.Failure(KICK_FAILED)

    val memberName = nameOf.displayName(member.uuid)
    clan.broadcast(memberKickedByTeamMessage(memberName))

    log.atInfo().log(
        "%s removed %s (%s) from clan %s (%s)",
        actor, member.uuid, member.role, clan.tag, clan.uuid
    )

    return ClanAdminResult.Success(adminMemberKickedMessage(memberName, clan.tag))
}

/**
 * Adds the clanless player [playerUuid] to [clan] as [role] without an invitation, announcing it to
 * the clan.
 */
suspend fun adminAdd(
    actor: ClanAdminActor,
    clan: Clan,
    playerUuid: UUID,
    playerName: String,
    role: ClanMemberRole
): ClanAdminResult {
    when (clan.addMember(playerUuid, role, actor.uuid)) {
        ClanMemberAddResult.AlreadyMember -> return ClanAdminResult.Failure(INVITEE_ALREADY_IN_CLAN)
        is ClanMemberAddResult.Success -> Unit
    }

    clan.broadcast(memberAddedByTeamMessage(playerName))

    log.atInfo().log(
        "%s added %s to clan %s (%s) as %s",
        actor, playerUuid, clan.tag, clan.uuid, role
    )

    return ClanAdminResult.Success(adminMemberAddedMessage(playerName, clan.tag, role))
}

/**
 * Sets the description of [clan], or removes it when [description] is `null`.
 */
suspend fun adminSetDescription(
    actor: ClanAdminActor,
    clan: Clan,
    description: String?
): ClanAdminResult {
    if (description != null) {
        descriptionError(description)?.let { return it.asFailure() }
    }

    clan.setDescription(description)

    log.atInfo().log(
        "%s changed the description of clan %s (%s) to %s",
        actor, clan.tag, clan.uuid, description
    )

    return ClanAdminResult.Success(adminDescriptionChangedMessage(clan.tag, removed = description == null))
}

/**
 * Sets the Discord invite of [clan], or removes it when [link] is `null`. Unlike the player command,
 * this ignores the active member threshold.
 */
suspend fun adminSetDiscord(
    actor: ClanAdminActor,
    clan: Clan,
    link: String?
): ClanAdminResult {
    if (link != null && !isValidDiscordInvite(link)) return ClanAdminResult.Failure(INVALID_DISCORD_LINK)

    clan.setDiscordInvite(link)

    log.atInfo().log(
        "%s changed the Discord invite of clan %s (%s) to %s",
        actor, clan.tag, clan.uuid, link
    )

    return ClanAdminResult.Success(adminDiscordLinkChangedMessage(clan.tag, link))
}

/**
 * Lists the pending invitations of [clan]: who was invited, by whom, and when.
 */
suspend fun adminInvitesMessage(
    clan: Clan,
    nameOf: suspend (UUID) -> String? = lookupName
): Component {
    val now = OffsetDateTime.now()
    val invites = clan.getPendingInvites()
        .sortedBy { it.createdAt }
        .map { invite ->
            AdminPendingInvite(
                inviteeName = nameOf.displayName(invite.invited),
                inviterName = nameOf.displayName(invite.invitedBy),
                timeAgo = formatTimeAgo(invite.createdAt, now)
            )
        }

    return adminPendingInvitesMessage(clan.tag, invites)
}

/**
 * Withdraws the pending invitation of [clan] for the player named by [target].
 */
suspend fun adminRevokeInvite(
    actor: ClanAdminActor,
    clan: Clan,
    target: String,
    nameOf: suspend (UUID) -> String? = lookupName,
    uuidOf: suspend (String) -> UUID? = lookupUuid
): ClanAdminResult {
    val invite = findPendingInviteOf(clan, target, uuidOf)
        ?: return ClanAdminResult.Failure(NO_PENDING_INVITE_FOR_PLAYER)

    if (!clan.revokeInvite(invite.invited)) return ClanAdminResult.Failure(NO_PENDING_INVITE_FOR_PLAYER)

    log.atInfo().log(
        "%s revoked the invitation of %s into clan %s (%s)",
        actor, invite.invited, clan.tag, clan.uuid
    )

    return ClanAdminResult.Success(adminInviteRevokedMessage(nameOf.displayName(invite.invited), clan.tag))
}

/**
 * Suggestions: the names of the members of the clan tagged [tag], or none for an unknown tag.
 */
suspend fun memberNamesOfTag(tag: String?): Set<String> {
    val clan = tag?.takeIf { it.isNotBlank() }?.let { Clan.byTag(it) } ?: return emptySet()

    return playerNames(clan.members.map { it.uuid })
}

/**
 * Suggestions: the names of the players invited into the clan tagged [tag], or none for an unknown
 * tag.
 */
suspend fun pendingInviteeNamesOfTag(tag: String?): Set<String> {
    val clan = tag?.takeIf { it.isNotBlank() }?.let { Clan.byTag(it) } ?: return emptySet()

    return playerNames(clan.getPendingInvites().map { it.invited })
}
