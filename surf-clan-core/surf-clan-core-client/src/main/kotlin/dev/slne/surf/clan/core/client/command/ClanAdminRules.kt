package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.clan.Clan
import dev.slne.clan.api.invite.ClanInvite
import dev.slne.clan.api.member.ClanMember
import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.clan.api.member.ClanMemberView
import dev.slne.surf.api.core.service.PlayerLookupService
import java.util.*

/** The longest description the `clan_clans.description` column holds. */
const val MAX_CLAN_DESCRIPTION_LENGTH = 255

/** The role names the admin commands accept, lowest first. */
val CLAN_ROLE_NAMES: List<String> = ClanMemberRole.entries.map { it.name.lowercase() }

val UNKNOWN_CLAN_ROLE = "Unbekannte Rolle. Erlaubt: ${CLAN_ROLE_NAMES.joinToString()}."

/**
 * Why the server team may not carry out an admin action on a clan.
 */
enum class ClanAdminError(val message: String) {
    ALREADY_HAS_ROLE("Der Spieler hat bereits diese Rolle."),
    CANNOT_DEMOTE_LAST_OWNER("Der letzte Besitzer kann nicht degradiert werden. Nutze /clan admin transferowner."),
    CANNOT_KICK_LAST_OWNER("Der letzte Besitzer kann nicht entfernt werden. Nutze /clan admin transferowner."),
    TRANSFER_NEEDS_SINGLE_OWNER("Der Clan hat nicht genau einen Besitzer. Nutze /clan admin setrole."),
    TARGET_ALREADY_OWNER("Der Spieler ist bereits Besitzer des Clans."),
    DESCRIPTION_TOO_LONG("Die Beschreibung darf höchstens $MAX_CLAN_DESCRIPTION_LENGTH Zeichen lang sein.")
}

/**
 * The members holding the [ClanMemberRole.OWNER] role. Normal clans have exactly one, but the
 * server team may appoint several.
 */
fun Clan.owners(): List<ClanMember> = members.filter { it.role == ClanMemberRole.OWNER }

private fun Clan.isLastOwner(member: ClanMemberView) =
    member.role == ClanMemberRole.OWNER && owners().size == 1

/**
 * Why [member] may not be given [newRole], or `null` when they may. A clan always keeps at least one
 * owner.
 */
fun Clan.setRoleError(member: ClanMemberView, newRole: ClanMemberRole): ClanAdminError? = when {
    member.role == newRole -> ClanAdminError.ALREADY_HAS_ROLE
    newRole != ClanMemberRole.OWNER && isLastOwner(member) -> ClanAdminError.CANNOT_DEMOTE_LAST_OWNER
    else -> null
}

/**
 * Why [member] may not be removed from this clan, or `null` when they may.
 */
fun Clan.kickError(member: ClanMemberView): ClanAdminError? =
    if (isLastOwner(member)) ClanAdminError.CANNOT_KICK_LAST_OWNER else null

/**
 * Why ownership may not be handed to [target], or `null` when it may. Only a clan with exactly one
 * owner can transfer, since otherwise it is unclear whose ownership is given up.
 */
fun Clan.transferOwnerError(target: ClanMemberView): ClanAdminError? = when {
    owners().size != 1 -> ClanAdminError.TRANSFER_NEEDS_SINGLE_OWNER
    target.role == ClanMemberRole.OWNER -> ClanAdminError.TARGET_ALREADY_OWNER
    else -> null
}

/**
 * Why [description] may not be stored, or `null` when it may. Too long a text is refused rather than
 * cut short.
 */
fun descriptionError(description: String): ClanAdminError? =
    if (description.length > MAX_CLAN_DESCRIPTION_LENGTH) ClanAdminError.DESCRIPTION_TOO_LONG else null

/**
 * The role named [input], ignoring case, or `null` for an unknown name.
 */
fun parseClanRole(input: String): ClanMemberRole? =
    ClanMemberRole.entries.firstOrNull { it.name.equals(input, ignoreCase = true) }

/** Resolves a player name to their uuid, the way [ClanMember.byName] does. */
internal val lookupUuid: suspend (String) -> UUID? = { PlayerLookupService.getUuid(it) }

/**
 * The member of [clan] named by [nameOrUuid], or `null` when nobody in [clan] matches.
 *
 * A name is resolved with a single lookup and then matched by uuid, rather than looking up the name
 * of every member. Unlike [findClanMember], this only looks inside [clan], so a player of another
 * clan is never picked up by mistake.
 */
suspend fun findMemberOf(
    clan: Clan,
    nameOrUuid: String,
    uuidOf: suspend (String) -> UUID? = lookupUuid
): ClanMember? {
    val uuid = resolveUuid(nameOrUuid, uuidOf) ?: return null

    return clan.members.firstOrNull { it.uuid == uuid }
}

/**
 * The pending invitation of [clan] for the player named by [nameOrUuid], or `null` when there is
 * none.
 */
suspend fun findPendingInviteOf(
    clan: Clan,
    nameOrUuid: String,
    uuidOf: suspend (String) -> UUID? = lookupUuid
): ClanInvite? {
    val uuid = resolveUuid(nameOrUuid, uuidOf) ?: return null

    return clan.getPendingInvites().firstOrNull { it.invited == uuid }
}

private suspend fun resolveUuid(nameOrUuid: String, uuidOf: suspend (String) -> UUID?): UUID? =
    runCatching { UUID.fromString(nameOrUuid) }.getOrNull() ?: uuidOf(nameOrUuid)
