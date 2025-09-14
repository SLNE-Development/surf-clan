@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.core.common.utils.formatComponent
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.ClanMemberListDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.createDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.buttons.createDemoteMemberButton
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.buttons.createKickMemberButton
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.buttons.createPromoteMemberButton
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
    selfClanPlayer: ClanPlayer,
    clanMember: ClanMember,
    clanMemberDisplayName: Component,
    clanPlayer: ClanPlayer,
    clan: Clan
): Dialog {
    val lastseen = clanPlayer.offlineCloudPlayer.lastSeen()?.formatComponent()
        ?: text("Unbekannt", Colors.VARIABLE_VALUE)

    return dialog {

        base {
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
            title { appendClanDialogTitle(buildText { variableValue(clan.name) }, clanMemberDisplayName) }
            body {
                plainMessage {
                    spacer("- ")
                    primary("Spieler: ")
                    append(clanMemberDisplayName)
                    appendNewline(2)

                    spacer("- ")
                    primary("Rang: ")
                    append(clanMember.role)
                    appendNewline(2)

                    spacer("- ")
                    primary("Beigetreten am: ")
                    append(clanMember.createdAt.formatComponent())
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

                if (clan.canPromote(selfClanPlayer, clanPlayer).isSuccess) {
                    action(
                        createPromoteMemberButton(
                            selfClanPlayer,
                            clanMember,
                            clanMemberDisplayName,
                            clanPlayer,
                            clan
                        )
                    )
                }

                if (clan.canDemote(selfClanPlayer, clanPlayer).isSuccess) {
                    action(
                        createDemoteMemberButton(
                            selfClanPlayer,
                            clanMember,
                            clanMemberDisplayName,
                            clanPlayer,
                            clan
                        )
                    )
                }

                if (clan.canKick(selfClanPlayer, clanPlayer)) {
                    action(createKickMemberButton(selfClanPlayer, clanMember, clanMemberDisplayName, clanPlayer, clan))
                }

                exitAction {
                    label { text("Zurück") }
                    tooltip { info("Klicke, um zur Mitgliederliste zurückzukehren.") }
                    width(200)

                    action {
                        playerCallback { player ->
                            plugin.launch {
                                player.showDialog(ClanMemberListDialog.createDialog(selfClanPlayer, clanPlayer, clan))
                            }
                        }
                    }
                }
            }
        }
    }
}