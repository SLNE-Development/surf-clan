@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.currentclan.CurrentClanDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.buttons.createClanMemberButton
import dev.slne.surf.clan.paper.dialogs.currentclan.createDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase

object ClanMemberListDialog

suspend fun ClanMemberListDialog.createDialog(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
): Dialog {
    val members = clan.members
    val membersWithDisplayName = members.associateWith { it.clanPlayer().offlineCloudPlayer.displayName() }

    return dialog {
        base {
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
            title { appendClanDialogTitle(buildText { variableValue(clan.name) }) }
            body {
                plainMessage {
                    val clanMembers = clan.members
                    primary("Der Clan ")
                    append(clan)
                    primary(" hat aktuell ")
                    variableValue(clanMembers.size)
                    primary(" Mitglieder.")
                }
            }
        }

        type {
            multiAction {
                columns(1)

                membersWithDisplayName.forEach { (member, displayName) ->
                    action(createClanMemberButton(selfClanPlayer, member, displayName, clanPlayer, clan))
                }

                exitAction {
                    label { text("Zurück") }
                    tooltip { info("Klicke, um zur Clan-Übersicht zurückzukehren.") }
                    width(200)

                    action {
                        playerCallback { player ->
                            plugin.launch {
                                player.showDialog(CurrentClanDialog.createDialog(selfClanPlayer, clanPlayer, clan))
                            }
                        }
                    }
                }
            }
        }
    }
}


