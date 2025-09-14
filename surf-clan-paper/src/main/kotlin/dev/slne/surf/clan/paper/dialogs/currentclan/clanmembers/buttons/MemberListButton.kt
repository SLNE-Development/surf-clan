@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.ClanMemberDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.ClanMemberListDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.list.createDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun ClanMemberDialog.createMemberListButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { text("Mitgliederliste") }
    tooltip { info("Klicke, um dir die Mitgliederliste deines Clans anzusehen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                player.showDialog(ClanMemberListDialog.createDialog(selfClanPlayer, clanPlayer, clan))
            }
        }
    }
}