@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.clan.paper.dialogs.createDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.ClanDisbandDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun ClanDisbandDialog.createClanDisbandSuccessNoticeButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer
) =
    actionButton {
        label { warning("Zurück") }
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