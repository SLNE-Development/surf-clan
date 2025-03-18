package dev.slne.clan.velocity.commands.subcommands.admin.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import dev.slne.surf.surfapi.core.api.messages.adventure.buildText
import net.kyori.adventure.text.Component

class ClanRefreshCommand(
    clanService: ClanService
) : CommandAPICommand("refresh") {
    init {
        withPermission("surf.clan.admin.refresh")

        anyExecutor { player, _ ->
            plugin.container.launch {
                clanService.refreshCache()

                player.sendMessage(buildText {
                    append(Component.text("Der Clan-Cache wurde aktualisiert.", Colors.SUCCESS))
                })
            }
        }
    }
}