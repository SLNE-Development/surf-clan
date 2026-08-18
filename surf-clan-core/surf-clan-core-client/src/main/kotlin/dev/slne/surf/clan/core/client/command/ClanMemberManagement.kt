package dev.slne.surf.clan.core.client.command

import dev.slne.clan.api.member.ClanMemberRole
import dev.slne.surf.api.core.messages.adventure.buildText

const val CANNOT_KICK_SELF = "Du kannst dich nicht selbst rauswerfen."
const val NO_KICK_PERMISSION = "Du hast keine Berechtigung, diesen Spieler aus dem Clan zu entfernen."
const val CANNOT_KICK_SAME_OR_HIGHER_ROLE =
    "Du kannst keine Spieler mit der selben oder einer höheren Rolle rauswerfen."
const val KICK_FAILED = "Fehler beim Entfernen des Spielers aus dem Clan."

const val CANNOT_PROMOTE_SELF = "Du kannst dich nicht selbst befördern."
const val NO_PROMOTE_PERMISSION = "Du hast keine Berechtigung, diesen Spieler zu befördern."
const val CANNOT_PROMOTE_SAME_OR_HIGHER_ROLE =
    "Du kannst keinen Spieler befördern, der den selben oder einen höheren Rang hat."
const val ALREADY_HIGHEST_ROLE = "Der Spieler hat bereits die höchste Rolle im Clan."

const val CANNOT_DEMOTE_SELF = "Du kannst dich nicht selbst degradieren."
const val NO_DEMOTE_PERMISSION = "Du hast keine Berechtigung, diesen Spieler zu degradieren."
const val CANNOT_DEMOTE_SAME_OR_HIGHER_ROLE =
    "Du kannst keinen Spieler degradieren, der den selben oder einen höheren Rang hat."
const val ALREADY_LOWEST_ROLE = "Der Spieler hat bereits den niedrigsten Rang."

const val ROLE_CHANGE_FAILED = "Fehler beim Ändern der Rolle des Spielers."

/**
 * The direction a member's role was moved in.
 */
enum class RoleChange(val pastTense: String) {
    PROMOTED(" befördert."),
    DEMOTED(" degradiert.")
}

/**
 * Announces that [memberName] was moved from [oldRole] to [newRole] by [actorName].
 */
fun memberRoleChangedMessage(
    memberName: String,
    actorName: String,
    oldRole: ClanMemberRole,
    newRole: ClanMemberRole,
    change: RoleChange
) = buildText {
    appendInfoPrefix()
    variableValue(memberName)
    info(" wurde durch ")
    variableValue(actorName)
    info(" von ")
    append(oldRole)
    info(" zu ")
    append(newRole)
    info(change.pastTense)
}

/**
 * Announces that [memberName] was removed from the clan by [actorName].
 */
fun memberKickedMessage(memberName: String, actorName: String) = buildText {
    appendInfoPrefix()
    variableValue(memberName)
    info(" wurde von ")
    variableValue(actorName)
    info(" aus dem Clan entfernt.")
}
