@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.clan.actions.DisbandClanAction
import dev.slne.surf.clan.api.common.clan.authorize
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.util.ClanAction
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.ClanDisbandDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.results.createClanDisbandNoticeDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun ClanDisbandDialog.createClanDisbandConfirmButton(
    selfPlayer: ClanPlayer,
    executorPlayer: ClanPlayer,
    clan: Clan
) = actionButton {
    label { warning("Clan Auflösen") }
    tooltip { info("Klicke, um deinen Clan aufzulösen.") }
    width(200)

    action {
        playerCallback { player ->
            plugin.launch {
                val result = clan.authorize<DisbandClanAction, ClanAction.EmptyArguments>(
                    executorPlayer,
                    ClanAction.EmptyArguments()
                )

                if (result.isError) error("Player ${selfPlayer.uuid} tried to disband clan ${clan.uuid} but has no permission to do so.")

                player.showDialog(
                    ClanDisbandDialog.createClanDisbandNoticeDialog(
                        selfPlayer = selfPlayer,
                        executorPlayer = executorPlayer,
                        clan = clan,
                        result = result
                    )
                )
            }
        }
    }
}