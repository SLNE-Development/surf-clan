package dev.slne.clan.velocity.commands.subcommands.player.subcommands.settings

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.kotlindsl.booleanArgument
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.clanPlayerService
import dev.slne.clan.velocity.plugin
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class ClanPlayerSettingInviteCommand : CommandAPICommand("invite") {
    init {
        withPermission("surf.clan.player.settings.invite")

        booleanArgument("accept", optional = true)

        executesPlayer(PlayerCommandExecutor { player, args ->
            plugin.container.launch {
                val accept = args.getOptionalUnchecked<Boolean>("accept").orElse(true)
                val clanPlayer = clanPlayerService.findClanPlayerByUuid(player.uniqueId)

                if (clanPlayer == null) {
                    player.sendMessage(Messages.notInClanComponent)

                    return@launch
                }

                clanPlayer.acceptsClanInvites = accept
                clanPlayerService.save(clanPlayer)

                if (accept) {
                    player.sendText {
                        appendPrefix()
                        info("Du hast Einladungen zu Clans ")
                        success("aktiviert")
                        info(".")
                    }
                } else {
                    player.sendText {
                        appendPrefix()
                        info("Du hast Einladungen zu Clans ")
                        error("deaktiviert")
                        info(".")
                    }
                }
            }
        })
    }
}