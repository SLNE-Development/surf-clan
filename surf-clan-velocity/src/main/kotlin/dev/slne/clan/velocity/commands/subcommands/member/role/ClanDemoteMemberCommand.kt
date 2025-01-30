package dev.slne.clan.velocity.commands.subcommands.member.role

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

class ClanDemoteMemberCommand(clanService: ClanService) : CommandAPICommand("demote") {
    init {
//        withPermission("surf.clan.demote")

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

            if (memberName == player.username) {
                player.sendMessage(buildMessage {
                    append(Component.text("Du kannst dich nicht selbst degradieren.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            val memberNameComponent =
                member.playerOrNull?.realName() ?: Component.text(memberName, COLOR_VARIABLE)

            if (!clan.hasPermission(player, ClanPermission.DEMOTE)) {
                player.sendMessage(buildMessage {
                    append(Component.text("Du hast keine Berechtigung, den Spieler ", COLOR_ERROR))
                    append(memberNameComponent)
                    append(Component.text(" im Clan ", COLOR_ERROR))
                    append(clanComponent(clan))
                    append(Component.text(" zu degradieren.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            if (!member.role.hasPreviousRole()) {
                player.sendMessage(buildMessage {
                    append(Component.text("Der Spieler ", COLOR_ERROR))
                    append(memberNameComponent)
                    append(Component.text(" hat bereits den niedrigsten Rang.", COLOR_ERROR))
                })

                return@PlayerCommandExecutor
            }

            val oldRole = member.role
            val newRole = member.role.previousRole()

            member.role = newRole

            val memberPromotedMessage = buildMessage {
                append(Component.text("Der Spieler ", COLOR_INFO))
                append(memberNameComponent)
                append(Component.text(" wurde durch ", COLOR_INFO))
                append(player.realName())
                append(Component.text(" von ", COLOR_INFO))
                append(oldRole.displayName)
                append(Component.text(" zu ", COLOR_INFO))
                append(newRole.displayName)
                append(Component.text(" degradiert.", COLOR_INFO))
            }

            plugin.container.launch {
                clanService.saveClan(clan)

                clan.members.forEach { member ->
                    member.player.sendMessage(memberPromotedMessage)
                }
            }
        })
    }
}