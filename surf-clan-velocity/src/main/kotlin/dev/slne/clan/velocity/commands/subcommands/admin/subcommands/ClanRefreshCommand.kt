package dev.slne.clan.velocity.commands.subcommands.admin.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.Colors
import net.kyori.adventure.text.Component

class ClanRefreshCommand(
    clanService: ClanService
) : CommandAPICommand("refresh") {
    init {
        withPermission("surf.clan.admin.refresh")

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val clan = player.findClan(clanService)

                clanService.refreshCache()

                player.sendMessage(buildMessage {
                    append(Component.text("Der Clan-Cache wurde aktualisiert.", Colors.SUCCESS))
                })
            }
        })
    }
}