@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.buttons

import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun MainClanDialog.createCreateClanButton() = actionButton {
    label { text("Einstellungen verlassen") }
    tooltip { info("Klicke, um das Menü zu schließen.") }
    width(200)

    action {
        playerCallback { player ->

        }
    }
}