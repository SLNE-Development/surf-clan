package dev.slne.clan.velocity.commands.subcommands.member.invite

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.kotlindsl.playerExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.Messages
import dev.slne.clan.core.service.clanPlayerService
import dev.slne.clan.core.service.clanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.VelocityMain.Companion.redisApi
import dev.slne.clan.velocity.commands.arguments.PlayerStringArgument
import dev.slne.clan.velocity.commands.arguments.playerStringArgument
import dev.slne.clan.velocity.extensions.findClan
import dev.slne.clan.velocity.extensions.hasPermission
import dev.slne.clan.velocity.plugin
import dev.slne.clan.velocity.redis.event.ClanInviteRedisEvent
import dev.slne.surf.surfapi.core.api.messages.adventure.sendText

class ClanInviteCommand : CommandAPICommand("invite") {
    init {
        withPermission("surf.clan.invite")
        playerStringArgument()

        playerExecutor { player, args ->
            plugin.container.launch {
                val playerClan = player.findClan()

                if (playerClan == null) {
                    player.sendMessage(Messages.notInClanComponent)
                    return@launch
                }

                if (!playerClan.hasPermission(player, ClanPermission.INVITE)) {
                    player.sendText {
                        appendPrefix()
                        error("Du hast keine Berechtigung, Spieler in den Clan einzuladen.")
                    }

                    return@launch
                }

                val invitedName = PlayerStringArgument.player(args)
                val invitedPlayer = clanPlayerService.findClanPlayerByName(invitedName)

                if (invitedPlayer == null) {
                    player.sendText {
                        appendPrefix()
                        error("Der Spieler konnte nicht gefunden werden.")
                    }
                    return@launch
                }

                val invitedPlayerClan = clanService.findClanByMember(invitedPlayer.uuid)

                if (invitedPlayerClan != null) {
                    player.sendText {
                        appendPrefix()
                        error("Der Spieler ist bereits in einem Clan.")
                    }

                    return@launch
                }

                if (!invitedPlayer.acceptsClanInvites) {
                    player.sendText {
                        appendPrefix()
                        error("Der Spieler nimmt keine Einladungen an.")
                    }
                    return@launch
                }

                val inviteResult = playerClan.invite(invitedPlayer.uuid, player.uniqueId)

                if (inviteResult) {
                    clanService.saveClan(playerClan)

                    player.sendText {
                        appendPrefix()
                        success("Du hast ")
                        variableValue(invitedPlayer.username)
                        success(" in den Clan ")
                        append(clanComponent(playerClan))
                        success(" eingeladen.")
                    }

                    redisApi.publishEvent(
                        ClanInviteRedisEvent(
                            player.username, invitedPlayer.uuid, playerClan.name
                        )
                    )
                } else {
                    player.sendText {
                        appendPrefix()
                        error("Der Spieler wurde bereits eingeladen.")
                    }
                }
            }
        }
    }
}