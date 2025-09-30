@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.member.ClanMember
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.ClanMemberListDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.ClanMemberManagementDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.management.createDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import net.kyori.adventure.text.Component

fun ClanMemberListDialog.createClanMemberButton(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
    targetPlayer: ClanPlayer,
    targetMember: ClanMember,
    targetDisplayName: Component,
    clan: Clan
) = actionButton {
    label(targetDisplayName)
    tooltip { info("Klicke, um dir das Mitglied anzusehen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                player.showDialog(
                    ClanMemberManagementDialog.createDialog(
                        selfPlayer = selfPlayer,
                        executorPlayer = executorPlayer,
                        targetPlayer = targetPlayer,
                        targetMember = targetMember,
                        targetDisplayName = targetDisplayName,
                        clan = clan
                    )
                )
            }
        }
    }
}