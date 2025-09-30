@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.CurrentClanDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.createDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.ClanDisbandDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun ClanDisbandDialog.createClanDisbandDenyButton(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { warning("Zurück") }
    tooltip { info("Klicke, um zurück zur Clan-Übersicht zu gelangen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                player.showDialog(
                    CurrentClanDialog.createDialog(
                        selfPlayer = selfPlayer,
                        executorPlayer = executorPlayer,
                        clan = clan
                    )
                )
            }
        }
    }
}