@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.buttons

import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun MainClanDialog.createCreateClanButton() = actionButton {
    label { success("Clan erstellen") }
    tooltip { info("Klicke, um einen Clan zu erstellen.") }
    width(200)

    action {
        playerCallback { player ->
            player.showDialog()
        }
    }
}