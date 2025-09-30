@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.buttons

import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.clan.paper.dialogs.create.CreateClanDialog
import dev.slne.surf.clan.paper.dialogs.create.createDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun MainClanDialog.createCreateClanButton(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
) = actionButton {
    label { success("Clan erstellen") }
    tooltip { info("Klicke, um einen Clan zu erstellen.") }
    width(200)

    action {
        playerCallback { player ->
            player.showDialog(
                CreateClanDialog.createDialog(
                    selfPlayer = selfPlayer,
                    executorPlayer = executorPlayer
                )
            )
        }
    }
}