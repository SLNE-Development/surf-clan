package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.api.core.messages.adventure.buildText

/**
 * Announces to the clan that the server team moved [memberName] from [oldRole] to [newRole].
 */
fun memberRoleSetByTeamMessage(
    memberName: String,
    oldRole: ClanMemberRole,
    newRole: ClanMemberRole
) = buildText {
    appendInfoPrefix()
    variableValue(memberName)
    info(" wurde vom Serverteam von ")
    append(oldRole)
    info(" zu ")
    append(newRole)
    info(" gesetzt.")
}

/**
 * Announces to the clan that the server team made [newOwnerName] its owner, and that [oldOwnerName]
 * is a leader now when [oldOwnerDemoted] is set.
 */
fun ownershipTransferredByTeamMessage(
    newOwnerName: String,
    oldOwnerName: String,
    oldOwnerDemoted: Boolean
) = buildText {
    appendInfoPrefix()
    variableValue(newOwnerName)
    info(" wurde vom Serverteam zum ")
    append(ClanMemberRole.OWNER)
    info(" ernannt.")
    if (oldOwnerDemoted) {
        info(" ")
        variableValue(oldOwnerName)
        info(" ist jetzt ")
        append(ClanMemberRole.LEADER)
        info(".")
    }
}

/**
 * Announces to the clan that the server team removed [memberName] from it.
 */
fun memberKickedByTeamMessage(memberName: String) = buildText {
    appendInfoPrefix()
    variableValue(memberName)
    info(" wurde vom Serverteam aus dem Clan entfernt.")
}

/**
 * Announces to the clan that the server team added [memberName] to it.
 */
fun memberAddedByTeamMessage(memberName: String) = buildText {
    appendInfoPrefix()
    variableValue(memberName)
    info(" wurde vom Serverteam zum Clan hinzugefügt.")
}

/**
 * Confirms to the admin that [memberName] of [clanTag] was moved from [oldRole] to [newRole].
 */
fun adminRoleSetMessage(
    memberName: String,
    clanTag: String,
    oldRole: ClanMemberRole,
    newRole: ClanMemberRole
) = buildText {
    appendSuccessPrefix()
    success("Rolle von ")
    variableValue(memberName)
    success(" in ")
    variableValue(clanTag)
    success(" von ")
    append(oldRole)
    success(" auf ")
    append(newRole)
    success(" gesetzt.")
}

/**
 * Confirms to the admin that [newOwnerName] owns [clanTag] now.
 *
 * When [oldOwnerDemoted] is not set, the clan is left with two owners and the admin is told how to
 * finish the transfer. [hasCustomTagColor] adds the hint that the tag color may be reset.
 */
fun adminOwnershipTransferredMessage(
    newOwnerName: String,
    oldOwnerName: String,
    clanTag: String,
    oldOwnerDemoted: Boolean,
    hasCustomTagColor: Boolean
) = buildText {
    appendSuccessPrefix()
    variableValue(newOwnerName)
    success(" ist jetzt ")
    append(ClanMemberRole.OWNER)
    success(" von ")
    variableValue(clanTag)
    success(".")
    if (oldOwnerDemoted) {
        success(" ")
        variableValue(oldOwnerName)
        success(" ist jetzt ")
        append(ClanMemberRole.LEADER)
        success(".")
    } else {
        appendNewline()
        appendWarningPrefix()
        variableValue(oldOwnerName)
        warning(" konnte nicht degradiert werden, der Clan hat jetzt zwei Besitzer. Nutze ")
        variableValue("/clan admin setrole $clanTag $oldOwnerName leader")
        warning(".")
    }
    if (hasCustomTagColor) {
        appendNewline()
        appendInfoPrefix()
        info("Hinweis: Die Tag-Farbe des Clans wird zurückgesetzt, falls der neue Besitzer die Rechte dafür nicht hat.")
    }
}

/**
 * Confirms to the admin that [memberName] was removed from [clanTag].
 */
fun adminMemberKickedMessage(memberName: String, clanTag: String) = buildText {
    appendSuccessPrefix()
    variableValue(memberName)
    success(" wurde aus ")
    variableValue(clanTag)
    success(" entfernt.")
}

/**
 * Confirms to the admin that [memberName] was added to [clanTag] as [role].
 */
fun adminMemberAddedMessage(memberName: String, clanTag: String, role: ClanMemberRole) = buildText {
    appendSuccessPrefix()
    variableValue(memberName)
    success(" wurde ")
    variableValue(clanTag)
    success(" als ")
    append(role)
    success(" hinzugefügt.")
}

/**
 * Confirms to the admin that the description of [clanTag] was changed, or removed when [removed] is
 * set.
 */
fun adminDescriptionChangedMessage(clanTag: String, removed: Boolean) = buildText {
    appendSuccessPrefix()
    success("Die Beschreibung von ")
    variableValue(clanTag)
    success(if (removed) " wurde entfernt." else " wurde geändert.")
}

/**
 * Confirms to the admin that the Discord link of [clanTag] was set to [link], or removed when it is
 * `null`.
 */
fun adminDiscordLinkChangedMessage(clanTag: String, link: String?) = buildText {
    appendSuccessPrefix()
    success("Der Discord-Link von ")
    variableValue(clanTag)
    if (link == null) {
        success(" wurde entfernt.")
    } else {
        success(" wurde auf ")
        variableValue(link)
        success(" gesetzt.")
    }
}

/**
 * One pending invitation as the admin sees it: who was invited, by whom, and how long ago.
 */
data class AdminPendingInvite(val inviteeName: String, val inviterName: String, val timeAgo: String)

/**
 * Lists the pending invitations of [clanTag] for the admin.
 */
fun adminPendingInvitesMessage(clanTag: String, invites: List<AdminPendingInvite>) = buildText {
    appendInfoPrefix()
    if (invites.isEmpty()) {
        info("Der Clan ")
        variableValue(clanTag)
        info(" hat keine offenen Einladungen.")
        return@buildText
    }

    info("Offene Einladungen von ")
    variableValue(clanTag)
    info(":")
    for (invite in invites) {
        appendNewline()
        appendInfoPrefix()
        variableValue(invite.inviteeName)
        info(" eingeladen von ")
        variableValue(invite.inviterName)
        info(", ")
        info(invite.timeAgo)
    }
}

/**
 * Confirms to the admin that the invitation of [inviteeName] into [clanTag] was withdrawn.
 */
fun adminInviteRevokedMessage(inviteeName: String, clanTag: String) = buildText {
    appendSuccessPrefix()
    success("Die Einladung von ")
    variableValue(inviteeName)
    success(" in ")
    variableValue(clanTag)
    success(" wurde zurückgezogen.")
}
