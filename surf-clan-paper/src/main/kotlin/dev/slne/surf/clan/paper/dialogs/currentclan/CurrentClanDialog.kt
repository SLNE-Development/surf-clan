@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.createMainMenuButton
import dev.slne.surf.clan.paper.dialogs.currentclan.buttons.createClanDisbandButton
import dev.slne.surf.clan.paper.dialogs.currentclan.buttons.createClanMembersButton
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import io.papermc.paper.dialog.Dialog

object CurrentClanDialog

fun CurrentClanDialog.createDialog(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
): Dialog = dialog {
    val member = clan.getMember(clanPlayer)
        ?: return@dialog error("Clan member ${clanPlayer.uuid} not found in their own clan ${clan.uuid}")

    base {
        title { appendClanDialogTitle(buildText { variableValue(clan.name) }) }
        body {
            plainMessage {
                primary("Du bist aktuell Mitglied in folgendem Clan:")
                variableValue(clan.name)
                primary(".")
                appendNewline(2)
                primary("Dein Rang: ")
                append(member.role.displayName)
                appendNewline(2)
                primary("Mitgliederanzahl: ")
                variableValue("${clan.members.size}")
            }
        }
    }

    type {
        multiAction {
            columns(1)
            action(createClanMembersButton(selfClanPlayer, clanPlayer, clan))

            if (clan.canDisband(clan, selfClanPlayer)) {
                action(createClanDisbandButton(selfClanPlayer, clanPlayer, clan))
            }

            exitAction(MainClanDialog.createMainMenuButton(selfClanPlayer, clanPlayer))
        }
    }
}