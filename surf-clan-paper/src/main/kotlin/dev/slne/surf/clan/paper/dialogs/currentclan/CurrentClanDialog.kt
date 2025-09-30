@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.DisbandClanAction
import dev.slne.surf.clan.api.common.clan.authorize
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.currentclan.buttons.createClanDisbandButton
import dev.slne.surf.clan.paper.dialogs.currentclan.buttons.createClanMembersButton
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase

object CurrentClanDialog

suspend fun CurrentClanDialog.createDialog(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
    clan: Clan
): Dialog {
    val member = clan.getMember(executorPlayer)
        ?: return error("Clan member ${executorPlayer.uuid} not found in their own clan ${clan.uuid}")

    val canDisband = clan.authorize<DisbandClanAction, ClanAction.EmptyArguments>(
        selfPlayer,
        ClanAction.EmptyArguments()
    )

    return dialog {
        base {
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
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
                action(
                    CurrentClanDialog.createClanMembersButton(
                        selfPlayer = selfPlayer,
                        executorPlayer = executorPlayer,
                        clan = clan
                    )
                )

                if (canDisband.isSuccess) {
                    action(
                        CurrentClanDialog.createClanDisbandButton(
                            selfPlayer = selfPlayer,
                            executorPlayer = executorPlayer,
                            clan = clan
                        )
                    )
                }

                exitAction(
                    MainClanDialog.createMainMenuButton(
                        selfPlayer = selfPlayer,
                        targetPlayer = executorPlayer
                    )
                )
            }
        }
    }
}