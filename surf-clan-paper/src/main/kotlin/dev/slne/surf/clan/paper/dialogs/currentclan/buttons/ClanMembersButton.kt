@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.CurrentClanDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.ClanMemberDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.clanmembers.createDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun CurrentClanDialog.createClanMembersButton(
    selfClanPlayer: ClanPlayer,
    clanPlayer: ClanPlayer,
    clan: Clan
) =
    actionButton {
        label { text("Mitglieder") }
        tooltip { info("Klicke, um dir die Mitglieder deines Clans anzusehen.") }
        width(200)

        action {
            playerCallback { player ->
                plugin.launch {
                    player.showDialog(ClanMemberDialog.createDialog(selfClanPlayer, clanPlayer, clan))
                }
            }
        }
    }