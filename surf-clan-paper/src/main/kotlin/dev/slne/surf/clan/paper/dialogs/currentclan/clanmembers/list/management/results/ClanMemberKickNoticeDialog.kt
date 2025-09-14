@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.results

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.clan.member.result.role.ClanMemberSetRoleResult
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.appendClanDialogTitle
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.ClanMemberManagementDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.createDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import io.papermc.paper.dialog.Dialog
import io.papermc.paper.registry.data.dialog.DialogBase
import net.kyori.adventure.text.Component

suspend fun ClanMemberManagementDialog.createClanMemberKickNoticeDialog(
    result: ClanMemberSetRoleResult,
    selfClanPlayer: ClanPlayer,
    clanMember: ClanMember,
    clanMemberDisplayName: Component,
    clanPlayer: ClanPlayer,
    clan: Clan
): Dialog {
    val resultMessage = result.asComponent()

    return dialog {
        base {
            afterAction(DialogBase.DialogAfterAction.WAIT_FOR_RESPONSE)
            title {
                appendClanDialogTitle(buildText { variableValue(clan.name) }, clanMemberDisplayName)
            }
            body {
                plainMessage {
                    append(resultMessage)
                }
            }
        }

        type {
            notice {
                action {
                    label { text("Zurück") }
                    tooltip { info("Klicke, um zur Spielerübersicht zurückzukehren.") }
                    width(200)

                    action {
                        playerCallback { player ->
                            plugin.launch {
                                player.showDialog(
                                    ClanMemberManagementDialog.createDialog(
                                        selfClanPlayer,
                                        clanMember,
                                        clanMemberDisplayName,
                                        clanPlayer,
                                        clan
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