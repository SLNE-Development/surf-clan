@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.buttons

import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.ClanMemberDialog
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun ClanMemberDialog.createSearchForMemberButton(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { text("Nach Mitglied suchen") }
    tooltip { info("Klicke, um nach einem Mitglied deines Clans zu suchen.") }
    width(200)

    action {
        playerCallback { player ->
            //TODO: dialog
        }
    }
}