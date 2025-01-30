package dev.slne.clan.velocity.commands.subcommands.member

import com.github.shynixn.mccoroutine.velocity.launch
import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.slne.clan.api.permission.ClanPermission
import dev.slne.clan.core.*
import dev.slne.clan.core.service.ClanService
import dev.slne.clan.core.utils.clanComponent
import dev.slne.clan.velocity.commands.arguments.ClanMemberArgument
import dev.slne.clan.velocity.commands.arguments.clanMemberArgument
import dev.slne.clan.velocity.extensions.*
import dev.slne.clan.velocity.plugin
import net.kyori.adventure.text.Component

class ClanKickMemberCommand(clanService: ClanService) : CommandAPICommand("kick") {
    init {
        withPermission("surf.clan.kick")

        clanMemberArgument(clanService)

        executesPlayer(PlayerCommandExecutor { player, args ->
            val memberName = args[0] as String
            val member = ClanMemberArgument.clanMember(clanService, player, args)
            val clan = player.findClan(clanService)

            if (clan == null) {
                player.sendMessage(Messages.notInClanComponent)

                return@PlayerCommandExecutor
            }

            if (member == null) {
                player.sendMessage(buildMessage {
                    append(Component.text("Der Spieler ", COLOR_ERROR))
                    append(Component.text(memberName, COLOR_VARIABLE))
                    append(Component.text(" ist nicht im Clan ", COLOR_ERROR))
                    append(clanComponent(clan))
                    append(Component.text(".", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            val memberNameComponent =
                member.playerOrNull?.realName() ?: Component.text(memberName, COLOR_VARIABLE)

            if (!clan.hasPermission(player, ClanPermission.KICK)) {
                player.sendMessage(buildMessage {
                    append(Component.text("Du hast keine Berechtigung, den Spieler ", COLOR_ERROR))
                    append(memberNameComponent)
                    append(Component.text(" aus dem Clan ", COLOR_ERROR))
                    append(clanComponent(clan))
                    append(Component.text(" zu entfernen.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            val memberKickedMessage = buildMessage {
                append(Component.text("Der Spieler ", COLOR_INFO))
                append(memberNameComponent)
                append(Component.text(" wurde von ", COLOR_INFO))
                append(player.realName())
                append(Component.text(" aus dem Clan ", COLOR_INFO))
                append(clanComponent(clan))
                append(Component.text(" entfernt.", COLOR_INFO))
            }

            plugin.container.launch {
                clan.removeMember(member)
                clanService.saveClan(clan)

                clan.members.forEach { member ->
                    member.player.sendMessage(memberKickedMessage)
                }

                member.playerOrNull?.sendMessage(memberKickedMessage)
            }
        })
    }
}