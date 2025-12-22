package dev.slne.clan.velocity.commands.subcommands.admin.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.slne.clan.velocity.clanConfigHolder
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class ClanReloadCommand : CommandAPICommand("reload") {
    init {
        withPermission("surf.clan.admin.reload")

        anyExecutor { player, _ ->
            plugin.container.launch {
                clanConfigHolder.reload()

                player.sendText {
                    appendPrefix()
                    success("Die Clan-Konfiguration wurde neu geladen.")
                }
            }
        }
    }
}