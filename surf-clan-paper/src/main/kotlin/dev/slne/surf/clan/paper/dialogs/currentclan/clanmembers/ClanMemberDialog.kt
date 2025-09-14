@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.currentclan.CurrentClanDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.buttons.createMemberListButton
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.buttons.createSearchForMemberButton
import dev.slne.surf.clan.paper.dialogs.currentclan.createDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import io.papermc.paper.registry.data.dialog.DialogBase

object ClanMemberDialog

fun ClanMemberDialog.createDialog(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
) = dialog {
    base {
        title { appendClanDialogTitle(buildText { variableValue(clan.name) }) }
        afterAction(DialogBase.DialogAfterAction.NONE)
    }

    type {
        multiAction {
            columns(2)


            action(createMemberListButton(selfClanPlayer, clanPlayer, clan))
            action(createSearchForMemberButton(selfClanPlayer, clanPlayer, clan))

            exitAction {
                label { text("Zurück") }
                tooltip { info("Klicke, um zur Clan-Übersicht zurückzukehren.") }
                width(200)

                action {
                    playerCallback { player ->
                        player.showDialog(CurrentClanDialog.createDialog(selfClanPlayer, clanPlayer, clan))
                    }
                }
            }
        }
    }
}

