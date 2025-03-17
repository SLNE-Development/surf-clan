package dev.slne.clan.velocity.commands.subcommands.admin.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText

object ClanRefreshCommand : CommandAPICommand("refresh") {
    init {
        withPermission("surf.clan.admin.refresh")

        anyExecutor { player, _ ->
            plugin.container.launch {
                ClanService.fetchClans()

                player.sendMessage(buildText {
                    appendPrefix()
                    
                    success("Der Clan-Cache wurde aktualisiert.")
                })
            }
        }
    }
}