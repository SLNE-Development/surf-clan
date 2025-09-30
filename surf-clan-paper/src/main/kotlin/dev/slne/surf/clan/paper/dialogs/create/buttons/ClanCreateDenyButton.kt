@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.create.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.clan.paper.dialogs.create.CreateClanDialog
import dev.slne.surf.clan.paper.dialogs.createDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun CreateClanDialog.createDenyButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
) = actionButton {
    label { success("Abbrechen") }
    tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                player.showDialog(MainClanDialog.createDialog(selfClanPlayer, clanPlayer))
            }
        }
    }
}