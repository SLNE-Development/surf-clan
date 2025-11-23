@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.ClanDisbandDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.createDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun ClanDisbandDialog.createClanDisbandErrorNoticeButton(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { warning("Zurück") }
    tooltip { info("Klicke, um zur Auflösung des Clans zurückzukehren.") }
    width(200)

    action {
        playerCallback { player ->
            player.showDialog(
                ClanDisbandDialog.createDialog(
                    selfPlayer = selfPlayer,
                    executorPlayer = executorPlayer,
                    clan = clan
                )
            )
        }
    }
}