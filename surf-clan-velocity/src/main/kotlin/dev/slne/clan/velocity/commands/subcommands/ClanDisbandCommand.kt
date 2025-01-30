package dev.slne.clan.velocity.commands.subcommands

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.COLOR_ERROR
import dev.slne.clan.core.Messages
import dev.slne.clan.core.buildMessage
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.extensions.player
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component

class ClanDisbandCommand(clanService: ClanService) : CommandAPICommand("disband") {
    init {
//        withPermission("surf.clan.disband")

        executesPlayer(PlayerCommandExecutor { player, args ->
            val clan = player.findClan(clanService)

            if (clan == null) {
                player.sendMessage(Messages.notInClanComponent)

                return@PlayerCommandExecutor
            }

            if (!clan.hasPermission(player, ClanPermission.DISBAND)) {
                player.sendMessage(buildMessage {
                    append(
                        Component.text("Du hast keine Berechtigung, den Clan ", COLOR_ERROR)
                    )
                    append(clanComponent(clan))
                    append(Component.text(" aufzulösen.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            val clanDisbandedMessage = buildMessage {
                append(Component.text("Der Clan ", COLOR_ERROR))
                append(clanComponent(clan))
                append(Component.text(" wurde aufgelöst.", COLOR_ERROR))
            }

            plugin.container.launch {
                clanService.deleteClan(clan)
                
                clan.members.forEach { member ->
                    member.player.sendMessage(clanDisbandedMessage)
                }
            }
        })
    }
}