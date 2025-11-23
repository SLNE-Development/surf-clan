@file:Suppress("UnstableApiUsage")
@file:OptIn(NmsUseWithCaution::class)

package dev.slne.surf.clan.paper.dialogs

import com.github.shynixn.mccoroutine.folia.launch
import dev.slne.surf.clan.api.common.player.ClanPlayer
import dev.slne.surf.clan.api.common.player.clanPlayer
import dev.slne.surf.clan.paper.dialogs.buttons.createCreateClanButton
import dev.slne.surf.clan.paper.dialogs.buttons.createCurrentClanButton
import dev.slne.surf.clan.paper.plugin
import dev.slne.surf.cloud.api.client.paper.player.toBukkitOfflinePlayer
import dev.slne.surf.cloud.api.client.paper.player.toCloudOfflinePlayer
import dev.slne.surf.surfapi.bukkit.api.dialog.base
import dev.slne.surf.surfapi.bukkit.api.dialog.builder.actionButton
import dev.slne.surf.surfapi.bukkit.api.dialog.clearDialogs
import dev.slne.surf.surfapi.bukkit.api.dialog.dialog
import dev.slne.surf.surfapi.bukkit.api.dialog.type
import dev.slne.surf.surfapi.bukkit.api.nms.NmsUseWithCaution
import dev.slne.surf.surfapi.core.api.messages.builder.SurfComponentBuilder
import io.papermc.paper.dialog.Dialog
import net.kyori.adventure.text.Component
import org.bukkit.OfflinePlayer

private const val CLAN_DIALOG_TITLE = "Clan System"

fun SurfComponentBuilder.appendClanDialogTitle(vararg parts: Component) {
    primary(CLAN_DIALOG_TITLE)

    parts.forEachIndexed { index, component ->
        if (index > 0) {
            spacer(" - ")
        }
        append(component)
    }
}

object MainClanDialog {
    suspend fun createDialog(
        selfPlayer: ClanPlayer,
        executorPlayer: ClanPlayer
    ): Dialog {
        val selfOfflinePlayer = selfPlayer.offlineCloudPlayer.toBukkitOfflinePlayer()
        val executorOfflinePlayer = executorPlayer.offlineCloudPlayer.toBukkitOfflinePlayer()

        return createDialog(selfOfflinePlayer, executorOfflinePlayer)
    }

    suspend fun createDialog(
        selfPlayer: OfflinePlayer,
        executorPlayer: OfflinePlayer
    ): Dialog {
        val selfCloudPlayer = selfPlayer.toCloudOfflinePlayer()
        val selfClanPlayer = selfCloudPlayer.clanPlayer()

        val executorCloudPlayer = executorPlayer.toCloudOfflinePlayer()
        val executorClanPlayer = executorCloudPlayer.clanPlayer()
        val clan = executorClanPlayer.clan

        return dialog {
            base {
                title { appendClanDialogTitle() }
            }

            type {
                multiAction {
                    columns(1)

                    if (clan != null) {
                        action(
                            MainClanDialog.createCurrentClanButton(
                                selfPlayer = selfClanPlayer,
                                executorPlayer = executorClanPlayer,
                                clan = clan
                            )
                        )
                    } else {
                        action(
                            MainClanDialog.createCreateClanButton(
                                selfPlayer = selfClanPlayer,
                                executorPlayer = executorClanPlayer
                            )
                        )
                    }
                    
                    exitAction {
                        label { text("Schließen") }
                        tooltip { info("Klicke, um das Menü zu schließen.") }
                        width(200)

                        action {
                            playerCallback { player ->
                                player.clearDialogs(true)
                            }
                        }
                    }
                }
            }
        }
    }

    fun createMainMenuButton(
        selfPlayer: ClanPlayer,
        targetPlayer: ClanPlayer
    ) = actionButton {
        label { text("Hauptmenü") }
        tooltip { info("Klicke, um zum Hauptmenü zurückzukehren.") }
        width(200)

        action {
            playerCallback { player ->
                plugin.launch {
                    player.showDialog(
                        createDialog(
                            selfPlayer = selfPlayer,
                            executorPlayer = targetPlayer
                        )
                    )
                }
            }
        }
    }
}