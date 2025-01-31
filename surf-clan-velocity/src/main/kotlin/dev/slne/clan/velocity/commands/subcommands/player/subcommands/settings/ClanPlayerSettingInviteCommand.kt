package dev.slne.clan.velocity.commands.subcommands.player.subcommands.settings

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.booleanArgument
import dev.jorel.commandapi.kotlindsl.getValue
import dev.slne.clan.core.COLOR_ERROR
import dev.slne.clan.core.COLOR_SUCCESS
import dev.slne.clan.core.Messages
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.service.ClanPlayerService
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component

class ClanPlayerSettingInviteCommand(
    clanService: ClanService,
    clanPlayerService: ClanPlayerService,
) : CommandAPICommand("invite") {
    init {
        withPermission("surf.clan.player.settings.invite")

        booleanArgument("accept")

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val accept: Boolean by args
                val clan = player.findClan(clanService)
                val clanPlayer = clanPlayerService.findClanPlayerByUuid(player.uniqueId)

                if (clan == null || clanPlayer == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                clanPlayer.acceptsClanInvites = accept
                clanPlayerService.save(clanPlayer)

                if (accept) {
                    player.sendMessage(buildMessage {
                        append(Component.text("Du hast Einladungen zu Clans ", COLOR_SUCCESS))
                        append(Component.text("aktiviert", COLOR_SUCCESS))
                        append(Component.text(".", COLOR_SUCCESS))
                    })
                } else {
                    player.sendMessage(buildMessage {
                        append(Component.text("Du hast Einladungen zu Clans ", COLOR_SUCCESS))
                        append(Component.text("deaktiviert", COLOR_ERROR))
                        append(Component.text(".", COLOR_SUCCESS))
                    })
                }
            }
        })
    }
}