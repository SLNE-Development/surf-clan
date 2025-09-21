@file:Suppress("UnstableApiUsage")

package dev.slne.surf.clan.paper.dialogs.currentclan.disband.buttons

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.clan.Clan
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.ClanDisbandDialog
import dev.slne.surf.clan.paper.dialogs.currentclan.disband.results.createClanDisbandNoticeDialog
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton

fun ClanDisbandDialog.createClanDisbandConfirmButton(
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
                plugin.launch {
                    if (!clan.canDisband(
                            clan,
                            selfClanPlayer
                        )
                    ) error("Player ${selfClanPlayer.uuid} tried to disband clan ${clan.uuid} but has no permission to do so.")

                    val result = clan.disbandClan(, clan)

                    player.showDialog(
                        ClanDisbandDialog.createClanDisbandNoticeDialog(
                            result,
                            selfClanPlayer,
                            clanPlayer,
                            clan
                        )
                    )
                }
            }
        }
    }