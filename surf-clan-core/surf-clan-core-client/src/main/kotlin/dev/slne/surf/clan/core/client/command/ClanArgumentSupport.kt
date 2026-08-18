package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.surf.api.core.service.PlayerLookupService
import kotlinx.coroutines.launch
import kotlinx.coroutines.supervisorScope
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.util.*
import java.util.concurrent.ConcurrentHashMap

private const val LOOKUP_CONCURRENCY = 64

const val CLAN_INVITE_ARGUMENT_NEEDS_PLAYER =
    "Cannot parse clan invite argument without player sender."

/**
 * The names of the clans [player] currently has a pending invitation to.
 */
suspend fun invitedClanNames(player: UUID): Set<String> {
    val invites = ClanInvite.pendingInvitesByPlayer(player)

    if (invites.isEmpty()) return emptySet()

    val clanNames = ConcurrentHashMap.newKeySet<String>()
    val semaphore = Semaphore(LOOKUP_CONCURRENCY)
    supervisorScope {
        for (invite in invites) {
            launch {
                semaphore.withPermit {
                    val clan = invite.getClan()
                    if (clan != null) {
                        clanNames.add(clan.name)
                    }
                }
            }
        }
    }

    return clanNames
}

/**
 * The names of the members of [player]'s clan, falling back to a member's uuid where their name is
 * unknown.
 */
suspend fun clanMemberNames(player: UUID): Set<String> {
    val clan = Clan.byPlayer(player) ?: return emptySet()

    val memberNames = ConcurrentHashMap.newKeySet<String>()
    val semaphore = Semaphore(LOOKUP_CONCURRENCY)
    supervisorScope {
        for (member in clan.members) {
            launch {
                semaphore.withPermit {
                    memberNames.add(
                        PlayerLookupService.getUsername(member.uuid) ?: member.uuid.toString()
                    )
                }
            }
        }
    }

    return memberNames
}

/**
 * Resolves a clan member from either their uuid or their name.
 */
suspend fun findClanMember(nameOrUuid: String): ClanMember? {
    val uuid = runCatching { UUID.fromString(nameOrUuid) }.getOrNull()

    return if (uuid != null) ClanMember.byUuid(uuid) else ClanMember.byName(nameOrUuid)
}
