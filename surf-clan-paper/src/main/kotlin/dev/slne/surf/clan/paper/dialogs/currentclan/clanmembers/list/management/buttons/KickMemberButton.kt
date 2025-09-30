@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.ClanMemberManagementDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.results.createClanMemberKickNoticeDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import net.kyori.adventure.text.Component

fun ClanMemberManagementDialog.createKickMemberButton(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
    targetPlayer: ClanPlayer,
    targetMember: ClanMember,
    targetDisplayName: Component,
    clan: Clan
) = actionButton {
    label { text("Entfernen") }
    tooltip { info("Klicke, um das Mitglied aus dem Clan zu entfernen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val result = clan.removeMember(targetPlayer, targetMember)

                player.showDialog(
                    createClanMemberKickNoticeDialog(
                        selfPlayer = selfPlayer,
                        executorPlayer = executorPlayer,
                        targetPlayer = targetPlayer,
                        targetMember = targetMember,
                        targetDisplayName = targetDisplayName,
                        clan = clan,
                        result = result
                    )
                )
            }
        }
    }
}