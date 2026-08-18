package dev.slne.surf.clan.core.client.command

import dev.slne.surf.api.core.messages.adventure.buildText
import dev.slne.surf.clan.core.clan.ClanImpl
import dev.slne.surf.clan.core.client.components.Components
import net.kyori.adventure.text.event.ClickEvent

const val NO_INVITE_PERMISSION = "Du hast keine Berechtigung, Spieler in den Clan einzuladen."
const val INVITEE_ALREADY_IN_CLAN = "Der Spieler ist bereits in einem Clan."
const val INVITEE_ALREADY_INVITED = "Der Spieler wurde bereits eingeladen."
const val INVITEE_DISABLED_INVITATIONS = "Der Spieler nimmt keine Einladungen an."
const val INVITE_ALREADY_DENIED = "Diese Einladung wurde bereits abgelehnt."

/**
 * Confirms to the inviter that [inviteeName] was invited into [clan].
 */
suspend fun inviteSentMessage(inviteeName: String, clan: ClanImpl) = buildText {
    appendSuccessPrefix()
    success("Du hast ")
    variableValue(inviteeName)
    success(" in den Clan ")
    append(Components.Clan.renderClanInformationHover(clan))
    success(" eingeladen.")
}

/**
 * Offers the invitee the invitation into [clanName], with buttons to accept or deny it.
 */
fun inviteReceivedMessage(inviterName: String, clanName: String) = buildText {
    appendInfoPrefix()
    info("Du wurdest von ")
    variableValue(inviterName)
    info(" in den Clan ")
    variableValue(clanName)
    info(" eingeladen. ")

    append {
        success("[Annehmen]")
        hoverEvent(buildText {
            info("Klicke hier, um die Einladung anzunehmen.")
        })
        clickEvent(ClickEvent.runCommand("/clan accept $clanName"))
    }

    appendSpace()

    append {
        error("[Ablehnen]")
        hoverEvent(buildText {
            info("Klicke hier, um die Einladung abzulehnen.")
        })
        clickEvent(ClickEvent.runCommand("/clan deny $clanName"))
    }
}

/**
 * Confirms to the invitee that they joined [clan].
 */
suspend fun inviteAcceptedMessage(clan: ClanImpl) = buildText {
    appendSuccessPrefix()
    success("Du bist dem Clan ")
    append(Components.Clan.renderClanInformationHover(clan))
    success(" beigetreten.")
}

/**
 * Announces to the clan that [memberName] joined it.
 */
fun memberJoinedMessage(memberName: String) = buildText {
    appendInfoPrefix()
    variableValue(memberName)
    info(" ist dem Clan beigetreten.")
}

/**
 * Confirms to the invitee that they turned the invitation down.
 */
fun inviteDeniedMessage() = buildText {
    appendSuccessPrefix()
    success("Du hast die Einladung abgelehnt.")
}

/**
 * Notifies the inviter that [inviteeName] turned their invitation down.
 */
fun inviteDeniedNotification(inviteeName: String) = buildText {
    appendInfoPrefix()
    variableValue(inviteeName)
    info(" hat deine Clan-Einladung abgelehnt.")
}
