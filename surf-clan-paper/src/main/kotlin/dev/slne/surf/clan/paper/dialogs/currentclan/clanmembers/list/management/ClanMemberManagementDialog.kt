@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.member.RemoveMemberArguments
import dev.slne.surf.clan.api.common.clan.authorize
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.core.common.utils.formatComponent
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.ClanMemberListDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.createDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.appendNewline
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import dev.slne.surf.surfapi.core.api.messages.adventure.text
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase
import net.kyori.adventure.text.Component

object ClanMemberManagementDialog

suspend fun ClanMemberManagementDialog.createDialog(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
    targetPlayer: ClanPlayer,
    targetMember: ClanMember,
    targetDisplayName: Component,
    clan: Clan
): Dialog {
    val lastseen = targetPlayer.offlineCloudPlayer.lastSeen()?.formatComponent()
        ?: text("Unbekannt", Colors.VARIABLE_VALUE)

    val canRemove = clan.authorize(
        executorPlayer,
        RemoveMemberArguments(
            target = targetPlayer,
            targetMember = targetMember
        )
    )

    return dialog {
        base {
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
            title {
                appendClanDialogTitle(
                    buildText { variableValue(clan.name) },
                    targetDisplayName
                )
            }
            body {
                plainMessage {
                    spacer("- ")
                    primary("Spieler: ")
                    append(targetDisplayName)
                    appendNewline(2)

                    spacer("- ")
                    primary("Rang: ")
                    append(targetMember.role)
                    appendNewline(2)

                    spacer("- ")
                    primary("Beigetreten am: ")
                    append(targetMember.createdAt.formatComponent())
                    appendNewline(2)

                    spacer("- ")
                    primary("Zuletzt aktiv am: ")
                    append(lastseen)
                }
            }
        }

        type {
            multiAction {
                columns(1)

                // FIXME: 30.09.2025 13:32 Add role button

                exitAction {
                    label { text("Zurück") }
                    tooltip { info("Klicke, um zur Mitgliederliste zurückzukehren.") }
                    width(200)

                    action {
                        playerCallback { player ->
                            plugin.launch {
                                player.showDialog(
                                    ClanMemberListDialog.createDialog(
                                        selfPlayer = executorPlayer,
                                        executorPlayer = targetPlayer,
                                        clan = clan
                                    )
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}