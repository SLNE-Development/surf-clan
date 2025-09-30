@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.buttons

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.MainClanDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.CurrentClanDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.createDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun MainClanDialog.createCurrentClanButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { text(clan.name) }
    tooltip { info("Klicke, um dir deinen Clan anzusehen.") }
    width(200)

    action {
        playerCallback { player ->
            player.showDialog(CurrentClanDialog.createDialog(selfClanPlayer, clanPlayer, clan))
        }
    }
}