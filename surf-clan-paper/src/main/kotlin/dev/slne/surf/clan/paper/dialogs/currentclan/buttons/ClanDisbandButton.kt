@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.buttons

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.CurrentClanDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.ClanDisbandDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.createDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun CurrentClanDialog.createClanDisbandButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
) =
    actionButton {
        label { warning("Clan Auflösen") }
        tooltip { info("Klicke, um deinen Clan aufzulösen.") }
        width(200)

        action {
            playerCallback { player ->
                player.showDialog(ClanDisbandDialog.createDialog(selfClanPlayer, clanPlayer, clan))
            }
        }
    }